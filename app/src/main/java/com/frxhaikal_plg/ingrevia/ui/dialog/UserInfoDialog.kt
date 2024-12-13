package com.frxhaikal_plg.ingrevia.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.ui.UserInformationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserInfoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.welcome)
            .setMessage(R.string.user_info_needed_message)
            .setPositiveButton(R.string.continue_text) { _, _ ->
                startActivity(Intent(requireContext(), UserInformationActivity::class.java))
                activity?.finishAffinity()
            }
            .setCancelable(false)
            .create()
    }

    companion object {
        const val TAG = "UserInfoDialog"
    }
}
