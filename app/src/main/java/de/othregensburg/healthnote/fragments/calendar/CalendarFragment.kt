package de.othregensburg.healthnote.fragments.calendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.othregensburg.healthnote.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH


class CalendarFragment : Fragment() {

        private var _binding: FragmentCalendarBinding? = null
        private val binding get() = _binding!!

        @SuppressLint("SimpleDateFormat", "WeekBasedYear")
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentCalendarBinding.inflate(inflater, container, false)
            val root: View = binding.root
            val cal = Calendar.getInstance()
            val etTitle = binding.etTitle
            val etDescription = binding.etDescription
            val etLocation = binding.etLocation
            val btnTime = binding.btnTime
            val btnDate = binding.btnDate
            val btnTransfer = binding.btnTransfer

            btnTime.setOnClickListener {
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    btnTime.text = SimpleDateFormat("HH:mm").format(cal.time)
                }
             /*   val dateSetListener = DatePickerDialog.OnDateSetListener { _, day,month, year ->
                    cal.set(Calendar.DAY_OF_MONTH, day)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.YEAR, year)
                    btnTime.text = SimpleDateFormat("dd:MM:yyyy").format(cal.getTime())
                }
                DatePickerDialog(requireContext(), dateSetListener, cal.get(DAY_OF_MONTH), cal.get(Calendar.YEAR), 1).show()
*/
                TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
            btnDate.setOnClickListener {
                val dateSetListener = DatePickerDialog.OnDateSetListener { _, day, month, year ->
                    cal.set(Calendar.DAY_OF_MONTH, day)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.YEAR, year)
                    btnDate.text = SimpleDateFormat("yyyy:MM:dd").format(cal.time)
                }
                DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), 1).show()
            }


            btnTransfer.setOnClickListener {
                addEvent(etTitle.text.toString(), etLocation.text.toString(), etDescription.text.toString(), btnTime.text.toString(), btnTime.text.toString() + 3600000)
            }

            return root
        }

    private fun addEvent(title: String, location: String, description: String, begin: String, end: String) {


        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
            type = "vnd.android.cursor.dir/event"
        }

       // if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent)
       // }

    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }*/

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


