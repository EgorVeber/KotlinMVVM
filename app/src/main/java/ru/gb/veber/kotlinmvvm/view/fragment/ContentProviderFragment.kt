package ru.gb.veber.kotlinmvvm.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.gb.veber.kotlinmvvm.databinding.FragmentContentProviderBinding
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterContacts
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view.adapter.OnContactsClickListener


const val REQUEST_CODE = 44

class ContentProviderFragment : Fragment(), OnContactsClickListener {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!

    private val adapter = AdapterContacts()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listContacts.adapter = adapter
        adapter.setOnContactsClickListener(this)
        checkPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContentProviderFragment()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    Log.d("TAG", "getContacts()")

                    getContacts()

                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    Log.d("TAG", "shouldShowRequestPermissionRationale")
                    androidx.appcompat.app.AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Bla bla Объяснение зачем")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    Log.d("TAG", "else")
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContacts()

                } else {
                    context?.let {
                        androidx.appcompat.app.AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Объяснение")
                            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    private fun getContacts() {

        var listContacts: MutableList<MyContacts> = mutableListOf()


        var handlerThread = Handler()
        Thread {
            context?.let {
                val contentResolver: ContentResolver = it.contentResolver


                val cursorWithContacts: Cursor? = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                )
                val cursorWithContacts2: Cursor? = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                )

                cursorWithContacts?.let { cursor ->
                    for (i in 0..cursor.count) {
                        val pos = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val pos2 =
                            cursorWithContacts2?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                        if (cursor.moveToPosition(i) && cursorWithContacts2?.moveToPosition(i) == true) {
                            val name = cursor.getString(pos)
                            val name2 = cursorWithContacts2?.getString(pos2!!)
                            listContacts.add(MyContacts(name, name2))
                        }
                    }
                }
                cursorWithContacts?.close()
                cursorWithContacts2?.close()
            }
            handlerThread.post {
                adapter.setWeather(listContacts)
            }
        }.start()
    }

    override fun onCityClick(contacts: MyContacts) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contacts.phone, null))
        startActivity(intent)
    }
}

data class MyContacts(var name: String, var phone: String)
