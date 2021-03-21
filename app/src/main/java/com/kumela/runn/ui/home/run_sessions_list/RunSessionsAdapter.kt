package com.kumela.runn.ui.home.run_sessions_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kumela.runn.data.db.run.RunSession

class RunSessionsAdapter(
    private val listener: Listener?
): ListAdapter<RunSession, RunSessionsAdapter.RunSessionsViewHolder>(DIFF), RunSessionItemViewMvc.Listener {

    fun interface Listener {
        fun onRunSessionClicked(runSession: RunSession)
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunSessionsViewHolder {
        val viewMvc = RunSessionItemViewMvc(LayoutInflater.from(parent.context), parent)
        viewMvc.registerListener(this)
        return RunSessionsViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: RunSessionsViewHolder, position: Int) {
        holder.viewMvc.bindRunSession(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id!!.toLong()
    }

    override fun onRunSessionClicked(runSession: RunSession) {
        listener?.onRunSessionClicked(runSession)
    }

    class RunSessionsViewHolder(val viewMvc: RunSessionItemViewMvc): RecyclerView.ViewHolder(viewMvc.rootView)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<RunSession>() {
            override fun areItemsTheSame(oldItem: RunSession, newItem: RunSession): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RunSession, newItem: RunSession): Boolean {
                return oldItem == newItem
            }
        }
    }
}