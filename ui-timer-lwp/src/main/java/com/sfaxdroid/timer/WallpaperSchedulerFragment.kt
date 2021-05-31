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
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.timer.TimerUtils.Companion.openAddWallpaperWithKeyActivity
import com.sfaxdroid.timer.TimerUtils.Companion.setWallpaperFromFile
import com.sfaxdroid.timer.databinding.FragmentWallpaperSchedulerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@AndroidEntryPoint
class WallpaperSchedulerFragment : Fragment(R.layout.fragment_wallpaper_scheduler) {

    private var scheduler: JobScheduler? = null

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    @Inject
    @Named("app-name")
    lateinit var appName: AppName

    lateinit var binding: FragmentWallpaperSchedulerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWallpaperSchedulerBinding.bind(view)
        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        scheduler =
            requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        initToolbar(screenName)
        initViewWithJobStatus()
        binding.radioGroup.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            val pendingJobs = scheduler?.allPendingJobs
            if (!pendingJobs.isNullOrEmpty() && pendingJobs.size > 0) removeJob()
        }
        binding.buttonAddLwp.setOnClickListener {
            loadWallpaperListFragment()
        }
        binding.buttonLwpList.setOnClickListener {
            loadFragment {
                replace(
                    binding.container.id,
                    WallpaperListFragment.newInstance(Constants.KEY_ADDED_LIST_TIMER_LWP),
                    "PlayerFragment.TAG"
                )
            }
        }
        binding.buttonActive.setOnClickListener {
            activeService()
        }
        binding.buttonClose.setOnClickListener {
            cancelService()
        }
    }

    private fun loadWallpaperListFragment() {
        loadFragment {
            replace(
                binding.container.id,
                WallpaperListFragment.newInstance(Constants.KEY_ADD_TIMER_LWP),
                "PlayerFragment.TAG"
            )
        }
    }

    private fun initToolbar(screeName: String?) {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = screeName
            setHomeButtonEnabled(appName != AppName.ChangedWall)
            setDisplayHomeAsUpEnabled(appName != AppName.ChangedWall)
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
        binding.buttonClose.apply {
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
        binding.buttonActive.apply {
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
                binding.radioOneHoure.isChecked =
                    true
            3600000 * 6.toLong() ->
                binding.radioSixHoure.isChecked =
                    true
            3600000 * 12.toLong() ->
                binding.radioHalfDay.isChecked =
                    true
            3600000 * 24.toLong() ->
                binding.radioOneDayHoure.isChecked =
                    true
            else -> binding.radioOneHoure.isChecked = true
        }
    }

    private fun getSelectedTime(): Long {
        val radioSelectedButton = binding.radioGroup.checkedRadioButtonId
        val timeBtn = binding.radioGroup.getChildAt(radioSelectedButton) as RadioButton?
        return when (timeBtn?.text.toString()) {
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

    private fun initTxtStatus(isActive: Boolean) {
        binding.txtNotForget.visibility = if (isActive) View.VISIBLE else View.GONE
        binding.txtStatus.text =
            if (isActive) getString(R.string.on_switch) else getString(R.string.off_switch)
        binding.txtStatus.setTextColor(
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
        binding.progressBarList.visibility = View.VISIBLE
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
                        binding.progressBarList.visibility = View.GONE
                    }
                }
            }
        } else {
            showDialogNoMinFiles()
        }
    }
}
