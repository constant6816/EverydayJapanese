package com.constant.japaneseeveryday.view.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.constant.japaneseeveryday.databinding.FragmentMonthlyBinding
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class MonthlyFragment : Fragment() {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        private const val EXTRA_YEAR = "EXTRA_YEAR"
        private const val EXTRA_MONTH = "EXTRA_MONTH"
        private const val EXTRA_SCHEDULES = "EXTRA_SCHEDULES"

        @JvmStatic
        fun newInstance(
            year: Int,
            month: Int,
            schedules: ArrayList<DateStatus>,
        ): MonthlyFragment {
            val fragment = MonthlyFragment()
            val args = Bundle()
            args.putInt(EXTRA_YEAR, year)
            args.putInt(EXTRA_MONTH, month)
            args.putParcelableArrayList(EXTRA_SCHEDULES, schedules)
            fragment.arguments = args
            return fragment
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: FragmentMonthlyBinding
    private lateinit var monthDate: LocalDateTime
    private var schedules: ArrayList<DateStatus>? = null

    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val year = it.getInt(EXTRA_YEAR)
            val month = it.getInt(EXTRA_MONTH)
            schedules = it.getParcelableArrayList(EXTRA_SCHEDULES)
            monthDate = LocalDateTime.of(year, month, 1, 0, 0)
            HHLog.d(TAG, "onCreate(), year = $year, month = $month")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initializeVariables()
        initializeViews(inflater, container)
        return binding.root
    }

    // Public Method
    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        binding =
            FragmentMonthlyBinding.inflate(inflater, container, false).apply {
                monthlyview.setSchedules(schedules!!)
                monthlyview.setMonthDate(monthDate)
            }
    }
}
