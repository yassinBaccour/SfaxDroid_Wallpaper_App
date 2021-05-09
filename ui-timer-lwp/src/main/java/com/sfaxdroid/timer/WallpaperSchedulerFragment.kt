package com.sfaxdroid.timer

import android.annotation.TargetApi
import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.timer.TimerUtils.Companion.openAddWallpaperWithKeyActivity
import com.sfaxdroid.timer.TimerUtils.Companion.setWallpaperFromFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@AndroidEntryPoint
class WallpaperSchedulerFragment : Fragment() {

    private var scheduler: JobScheduler? = null

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    private lateinit var progressBar: ProgressBar
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonAddLwp: Button
    private lateinit var buttonLWPList: Button
    private lateinit var buttonActive: Button
    private lateinit var buttonClose: Button
    private lateinit var txtNotForget: TextView
    private lateinit var txtstatus: TextView
    private lateinit var container: FrameLayout
    private lateinit var toolbar: Toolbar
    private lateinit var radioOneHour: RadioButton
    private lateinit var radioSixHour: RadioButton
    private lateinit var radioTwelveHour: RadioButton
    private lateinit var radioOneDay: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpaper_scheduler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        scheduler =
            requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        progressBar = view.findViewById(R.id.progressBar)
        radioGroup = view.findViewById(R.id.radioGroup)
        buttonAddLwp = view.findViewById(R.id.buttonAddLwp)
        buttonLWPList = view.findViewById(R.id.buttonLWPList)
        buttonActive = view.findViewById(R.id.buttonActive)
        buttonClose = view.findViewById(R.id.buttonClose)
        txtNotForget = view.findViewById(R.id.txtNotForget)
        txtstatus = view.findViewById(R.id.txtstatus)
        container = view.findViewById(R.id.container)
        radioOneHour = view.findViewById(R.id.radioOneHoure)
        radioSixHour = view.findViewById(R.id.radioSixHoure)
        radioTwelveHour = view.findViewById(R.id.radioDouzeHoure)
        radioOneDay = view.findViewById(R.id.radioOneDayHoure)
        toolbar = view.findViewById(R.id.toolbar)

        initToolbar(screenName)

        initViewWithJobStatus()

        radioGroup.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            val pendingJobs = scheduler?.allPendingJobs
            if (!pendingJobs.isNullOrEmpty() && pendingJobs.size > 0) removeJob()
        }
        buttonAddLwp.setOnClickListener {
            loadWallpaperListFragment()
        }
        buttonLWPList.setOnClickListener {
            loadFragment {
                replace(
                    container.id,
                    WallpaperListFragment.newInstance(Constants.KEY_ADDED_LIST_TIMER_LWP),
                    "PlayerFragment.TAG"
                )
            }
        }
        buttonActive.setOnClickListener {
            activeService()
        }
        buttonClose.setOnClickListener {
            cancelService()
        }
    }

    private fun loadWallpaperListFragment() {
        loadFragment {
            replace(
                container.id,
                WallpaperListFragment.newInstance(Constants.KEY_ADD_TIMER_LWP),
                "PlayerFragment.TAG"
            )
        }
    }

    private fun initToolbar(screeName: String?) {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = screeName
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViewWithJobStatus() {
        val allPendingJobs = scheduler?.allPendingJobs

        if (!allPendingJobs.isNullOrEmpty() && allPendingJobs.size > 0) {
            initTimeCheckBox(allPendingJobs[0].intervalMillis)
            initTxtStatus(true)
            initBtnActive(true)
        } else {
            initTimeCheckBox(0)
            initTxtStatus(false)
            initBtnClose(false)
        }
    }

    private fun cancelService() {
        removeJob()
        initBtnClose(false)
        initBtnActive(false)
    }

    private fun activeService() {
        if (fileManager.getPermanentDirListFiles().size > 3) {
            activeJob()
            initBtnActive(true)
            initBtnClose(true)
        } else {
            showMessageOKCancel(
                requireContext().getString(R.string.add_wallpaper_messages),
                { _: DialogInterface?, _: Int -> openWallpaperList() },
                requireActivity()
            )
        }
    }

    private fun openWallpaperList() {
        loadWallpaperListFragment()
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener,
        ac: Activity
    ) {
        AlertDialog.Builder(ac)
            .setMessage(message)
            .setPositiveButton(
                ac.getString(com.sfaxdroid.base.R.string.permission_accept_click_button),
                okListener
            )
            .setNegativeButton(
                ac.getString(com.sfaxdroid.base.R.string.permission_deny_click_button),
                null
            )
            .create()
            .show()
    }

    private fun initBtnClose(isJobActive: Boolean) {
        buttonClose.apply {
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                ResourcesCompat.getDrawable(
                    resources,
                    if (isJobActive) R.mipmap.ic_close_img else R.mipmap.ic_close_img_on,
                    context.theme
                ),
                null,
                null
            )
            setTextColor(
                if (isJobActive) Color.WHITE else resources.getColor(R.color.timer_flu_green)
            )
        }
    }

    private fun initBtnActive(isJobActive: Boolean) {
        buttonActive.apply {
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                ResourcesCompat.getDrawable(
                    resources,
                    if (isJobActive) R.mipmap.ic_active_img_on else R.mipmap.ic_active_img,
                    context.theme
                ),
                null,
                null
            )
            setTextColor(if (isJobActive) resources.getColor(R.color.timer_flu_green) else Color.WHITE)
        }
    }

    private fun initTimeCheckBox(time: Long) {
        when (time) {
            3600000L ->
                radioOneHour.isChecked =
                    true
            3600000 * 6.toLong() ->
                radioSixHour.isChecked =
                    true
            3600000 * 12.toLong() ->
                radioTwelveHour.isChecked =
                    true
            3600000 * 24.toLong() ->
                radioOneDay.isChecked =
                    true
            else -> radioOneHour.isChecked = true
        }
    }

    private fun getSelectedTime(): Long {

        val radioSelectedButton =
            view?.findViewById<View>(radioGroup.checkedRadioButtonId) as RadioButton

        return when (radioSelectedButton.text.toString()) {
            getString(R.string.one_houre) -> 3600000
            getString(
                R.string.six_houre
            ) -> 3600000 * 6
            getString(R.string.twelve_hour) -> 3600000 * 12
            getString(
                R.string.one_day
            ) -> 3600000 * 24
            else -> 30000
        }
    }

    private fun showDialogNoMinFiles() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_wallpaper_title))
            .setMessage(getString(R.string.add_wallpaper_min_message))
            .setPositiveButton(
                getString(R.string.add_image_ok)
            ) { _: DialogInterface?, _: Int ->
                openAddWallpaperWithKeyActivity(requireContext(), Constants.KEY_ADD_TIMER_LWP)
            }
            .setNegativeButton(
                getString(R.string.cancel_adding_images)
            ) { _: DialogInterface?, _: Int -> }
            .show()
    }

    private fun removeJob() {
        scheduler?.cancelAll()
        initTxtStatus(false)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            // finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun initTxtStatus(isActive: Boolean) {
        txtNotForget.visibility = if (isActive) View.VISIBLE else View.GONE
        txtstatus.text =
            if (isActive) getString(R.string.on_switch) else getString(R.string.off_switch)
        txtstatus.setTextColor(
            if (isActive) resources.getColor(R.color.timer_green) else resources.getColor(
                R.color.timer_red
            )
        )
    }

    private fun scheduleJob(): Int =
        scheduler?.schedule(
            JobInfo.Builder(
                1,
                ComponentName(
                    requireContext(),
                    RetrieveWallpaperService::class.java
                )
            )
                .setPeriodic(getSelectedTime())
                .setPersisted(true)
                .build()
        ) ?: JobScheduler.RESULT_FAILURE

    private fun activeJob() {
        progressBar.visibility = View.VISIBLE
        if (fileManager.getPermanentDirListFiles().size > 3) {
            GlobalScope.launch(Dispatchers.Default) {
                val status = scheduleJob()
                if (status == JobScheduler.RESULT_SUCCESS) {
                    setWallpaperFromFile(
                        requireContext(),
                        fileManager.getPermanentDirListFiles(),
                        deviceManager.getScreenWidthPixels(),
                        deviceManager.getScreenHeightPixels()
                    )
                    GlobalScope.launch(Dispatchers.Main) {
                        initTxtStatus(true)
                        progressBar.visibility = View.GONE
                    }
                }
            }
        } else {
            showDialogNoMinFiles()
        }
    }
}
