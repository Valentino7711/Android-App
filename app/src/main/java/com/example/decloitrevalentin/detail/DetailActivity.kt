package com.example.decloitrevalentin.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decloitrevalentin.detail.ui.theme.DECLOITREValentinTheme
import com.example.decloitrevalentin.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initTask = intent.getSerializableExtra("task") as Task? ?:Task(id = UUID.randomUUID().toString(), title = "", description = "")
        setContent {
            DECLOITREValentinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail(onValidate = {task ->
                        intent.putExtra("task", task)
                        setResult(RESULT_OK, intent)
                        finish()
                    },
                        initTask = initTask)
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier, onValidate: (Task) -> Unit, initTask: Task) {
    var task by remember { mutableStateOf(initTask) }
    Column(
        modifier = Modifier
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task Detail",
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            value = task.title,
            onValueChange = { newTitle -> task = task.copy(title = newTitle) },
            label = { Text("Entrez votre texte") }
        )
        OutlinedTextField(
            value = task.description,
            onValueChange = { newDescription -> task = task.copy(description = newDescription) },
            label = { Text("Entrez votre texte") }
        )
        Button(
            onClick = {
                onValidate(Task(task.id, task.title, task.description))
                      /* Action à effectuer lors du clic sur le bouton */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Validation", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    DECLOITREValentinTheme {
        Detail(onValidate = {task -> }, initTask = Task(id = "prout", title="men fou"))
    }
}