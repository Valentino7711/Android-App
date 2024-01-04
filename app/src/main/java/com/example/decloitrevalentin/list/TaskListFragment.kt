package com.example.decloitrevalentin.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.decloitrevalentin.R
import com.example.decloitrevalentin.data.Api
import com.example.decloitrevalentin.detail.DetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

class TaskListFragment : Fragment() {

    private val viewModel: TasksListViewModel by viewModels()

    private val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            /*taskList = taskList - task
            adapter.submitList(taskList)*/
            viewModel.remove(task)
        }
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
    }

    private val adapter = TaskListAdapter(adapterListener)

    //private var taskList = emptyList<Task>()

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            /*taskList = taskList + task
            adapter.submitList(taskList)*/
            viewModel.add(task)
        }
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            /*taskList = taskList.map { if (it.id == task.id) task else it }
            adapter.submitList(taskList)*/
            viewModel.edit(task)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleScope.launch {
            /*taskList = Api.taskWebService.fetchTasks().body()!!
            adapter.submitList(taskList);*/
            viewModel.refresh()
        }
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = adapter

        val buttonCreate = view.findViewById<FloatingActionButton>(R.id.imageButton)
        buttonCreate.setOnClickListener {
            // Instanciation d'un objet task avec des données préremplies:
            /*val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList);*/
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                //taskList = newList
                adapter.submitList(newList)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            view?.findViewById<TextView>(R.id.textView2)?.text = user.name
            //Log.i(user.name, "Message Info")
            viewModel.refresh() // on demande de rafraîchir les données sans attendre le retour directement
        }
    }


}