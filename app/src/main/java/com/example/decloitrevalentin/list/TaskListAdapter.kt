package com.example.decloitrevalentin.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.decloitrevalentin.R


object MyItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id// comparaison: est-ce la même "entité" ? => même id?
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem == newItem// comparaison: est-ce le même "contenu" ? => mêmes valeurs? (avec data class: simple égalité)
    }
}


// l'IDE va râler ici car on a pas encore implémenté les méthodes nécessaires
class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyItemsDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val taskTextView: TextView? = itemView.findViewById(R.id.task_title)

        private val taskDescriptionTextView: TextView? = itemView.findViewById(R.id.task_description)

        fun bind(task: Task) {
            // on affichera les données ici
            taskTextView?.text = task.title
            taskDescriptionTextView?.text = task.description
            itemView.findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
                listener.onClickDelete(task)
            }
            itemView.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
                listener.onClickEdit(task)
            }
        }
    }


}