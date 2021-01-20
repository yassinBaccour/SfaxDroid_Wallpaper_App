package com.sfaxdroid.timer

import android.annotation.TargetApi
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
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.timer.Utils.Companion.openAddWallpaperWithKeyActivity
import com.sfaxdroid.timer.Utils.Companion.setWallpaperFromFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallpaper_scheduler.*
import kotlinx.coroutines.*
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@AndroidEntryPoint
class WallpaperSchedulerFragment : Fragment() {

    private var scheduler: JobScheduler? = null

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpaper_scheduler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        scheduler =
            requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        initViewWithJobStatus()

        radioGroup.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            val pendingJobs = scheduler?.allPendingJobs
            if (!pendingJobs.isNullOrEmpty() && pendingJobs.size > 0) removeJob()
        }
        buttonAddLwp.setOnClickListener {
            openAddWallpaperWithKeyActivity(
                requireContext(), Constants.KEY_ADD_TIMER_LWP
            )
        }
        buttonLWPList.setOnClickListener {
            openAddWallpaperWithKeyActivity(
                requireContext(), Constants.KEY_ADDED_LIST_TIMER_LWP
            )
        }
        buttonActive.setOnClickListener {
            activeService()
        }
        buttonClose.setOnClickListener {
            cancelService()
        }
    }

    private fun initViewWithJobStatus() {
        val allPendingJobs = scheduler?.allPendingJobs

        if (!allPendingJobs.isNullOrEmpty() && allPendingJobs.size > 0) {
            val jobInfo = allPendingJobs[0]
            initTimeCheckBox(jobInfo.intervalMillis)
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
            com.sfaxdroid.base.utils.Utils.showSnackMessage(
                rootLayout,
                getString(R.string.add_wallpaper_messages)
            )
        }
    }

    private fun initBtnClose(isJobActive: Boolean) {
        buttonClose?.apply {
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
                resources.getColor(if (isJobActive) Color.WHITE else R.color.timer_flu_green),
            )
        }
    }

    private fun initBtnActive(isJobActive: Boolean) {
        buttonActive?.apply {
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
            3600000L -> radioOneHoure?.isChecked =
                true
            3600000 * 6.toLong() -> radioSixHoure?.isChecked =
                true
            3600000 * 12.toLong() -> radioDouzeHoure?.isChecked =
                true
            3600000 * 24.toLong() -> radioOneDayHoure?.isChecked =
                true
            else -> radioOneHoure?.isChecked = true
        }
    }

    private fun getSelectedTime(): Long {

        val radioSelectedButton =
            view?.findViewById<View>(radioGroup?.checkedRadioButtonId!!) as RadioButton

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

    private fun initToolbar() {
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            //finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun initTxtStatus(isActive: Boolean) {
        txtNotForget?.visibility = if (isActive) View.VISIBLE else View.GONE
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
        progressBar?.visibility = View.VISIBLE
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
                        com.sfaxdroid.base.utils.Utils.showSnackMessage(
                            rootLayout,
                            getString(R.string.auto_change_on)
                        )
                    }
                }
            }
        } else {
            showDialogNoMinFiles()
        }
    }

}