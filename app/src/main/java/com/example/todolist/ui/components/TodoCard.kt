package com.example.todolist.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.domain.model.TaskItem

@Composable
fun TodoCard(
    task: TaskItem,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)
    val textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
    val nameColor = if (task.completed) Color(0xFF9E9E9E) else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFF393939), shape = cardShape),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1B1B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = task.name.orEmpty(),
                    style = TextStyle(
                        color = nameColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = textDecoration
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!task.description.isNullOrBlank()) {
                    Text(
                        text = task.description,
                        style = TextStyle(
                            color = Color(0xFF9E9E9E),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textDecoration = textDecoration
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Checkbox(
                checked = task.completed,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF0F62FE),
                    uncheckedColor = Color(0xFF3F4759),
                    checkmarkColor = Color.White
                )
            )
        }
    }
}
