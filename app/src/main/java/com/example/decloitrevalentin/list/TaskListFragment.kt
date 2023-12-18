package com.example.decloitrevalentin.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.decloitrevalentin.R
import com.example.decloitrevalentin.detail.DetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment() {

    private val adapter = TaskListAdapter()

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            taskList = taskList + task
            adapter.submitList(taskList)
        }
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            taskList = taskList.map { if (it.id == task.id) task else it }
            adapter.submitList(taskList)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter.submitList(taskList);
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val intent = Intent(context, DetailActivity::class.java)
        recyclerView.adapter = adapter

        val buttonCreate = view.findViewById<FloatingActionButton>(R.id.imageButton)
        buttonCreate.setOnClickListener {
            // Instanciation d'un objet task avec des données préremplies:
            /*val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList);*/
            createTask.launch(intent)
        }

        adapter.onClickDelete = {task ->
            taskList = taskList - task
            adapter.submitList(taskList)
        }

        adapter.onClickEdit = {task ->
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
    }


}