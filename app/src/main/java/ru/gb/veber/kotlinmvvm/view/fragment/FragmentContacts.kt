package ru.gb.veber.kotlinmvvm.view.fragment

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentContentProviderBinding
import ru.gb.veber.kotlinmvvm.model.hide
import ru.gb.veber.kotlinmvvm.model.show
import ru.gb.veber.kotlinmvvm.view.MainActivity
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterContacts
import ru.gb.veber.kotlinmvvm.view.adapter.OnContactsClickListener


const val REQUEST_CODE = 44
private const val CONTACT_ID = ContactsContract.Contacts._ID
private const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
private const val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
private const val PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
private const val PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID

class FragmentContacts : Fragment(), OnContactsClickListener {
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
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.listContacts.adapter = adapter
        adapter.setOnContactsClickListener(this)
        checkPermission()
    }

    override fun onContactsClick(contacts: MyContacts) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contacts.phone, null))
        startActivity(intent)
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
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
                    binding.listContacts.hide()
                    binding.emptyView.show()
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
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
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
                            Log.d("TAG", "getContacts() called with: name2 = $name2")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_content_provider).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
                (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentContacts()
    }
}

data class MyContacts(var name: String, var phone: String)
