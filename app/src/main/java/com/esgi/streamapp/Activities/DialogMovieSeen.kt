package com.esgi.streamapp.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.esgi.streamapp.R
import kotlinx.android.synthetic.main.dialog_movie_seen.view.*


class DialogMovieSeen : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.dialog_movie_seen, container, false)

        view.btnRestart.setOnClickListener {
            (activity as PlayerActivity?)?.resumeMovie(false)
            dismiss()
        }

        view.btnResume.setOnClickListener {
            (activity as PlayerActivity?)?.resumeMovie(true)
            dismiss()
        }
        return view
    }
}