package com.common.studyhelper

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.common.studyhelper.databinding.FragmentFirstBinding
import com.common.studyhelper.helper.GPTAPIHandler

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this.requireContext()))
        }

        val py = Python.getInstance()

        val youTubeTranscriptApi = py.getModule("transcript")//doesn't work with version 34

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.textViewSummary.movementMethod = ScrollingMovementMethod();
        binding.summarizeButton.setOnClickListener {

            var transcript = ""

            val link = binding.editTextLink.text
            try {
                transcript = youTubeTranscriptApi.callAttr("gettranscript",
                    link.toString()
                ).toString()
            } catch (e: PyException) {
                Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }


            val activity = context as MainActivity
            activity.setTranscript(transcript)

            val gptapihandler = GPTAPIHandler()


            gptapihandler.summarizeText(transcript){ response ->

                activity?.runOnUiThread{
                    binding.textViewSummary.text = response

                    binding.layoutQuestion.visibility = View.VISIBLE
                }

            }

        }
        binding.buttonQuestion.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.buttonFirst.setOnClickListener {
        //    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}