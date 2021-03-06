package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.dialog_info.*
import ru.gb.veber.kotlinmvvm.databinding.DialogInfoBinding
import ru.gb.veber.kotlinmvvm.model.Info
import ru.gb.veber.kotlinmvvm.model.addDegree
import ru.gb.veber.kotlinmvvm.view_model.ViewModelDialog

class FragmentDialog : DialogFragment() {

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModelDialog: ViewModelDialog by lazy {
        ViewModelProvider(requireActivity()).get(ViewModelDialog::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelDialog.getWeatherData().observe(this) { initView(it) }
    }

    private fun initView(info: Info) {
        info.let {
            it.apply {
                lat_tv.text = lat.toString().addDegree()
                lon_tv.text = lon.toString().addDegree()
                timezone.text = tzinfo?.name
                pressure_tv.text = def_pressure_mm.toString() + " мм рт"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}