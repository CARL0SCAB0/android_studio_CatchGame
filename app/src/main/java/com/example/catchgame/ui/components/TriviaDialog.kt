package com.example.catchgame.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catchgame.game.model.TriviaQuestion

@Composable
fun TriviaDialog(
    question: TriviaQuestion,
    timeLeftSeconds: Int,
<<<<<<< HEAD
    feedbackMessage: String?,
    isAnswerLocked: Boolean,
=======
>>>>>>> ac753394584083abda6cebc57966da7fe7c7b46d
    onAnswerSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
<<<<<<< HEAD
            // No permitir cerrar el diálogo sin resolver la trivia.
=======
            // No se permite cerrar sin responder.
>>>>>>> ac753394584083abda6cebc57966da7fe7c7b46d
        },
        title = {
            Text(
                text = "Última oportunidad",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
<<<<<<< HEAD
                    text = "Categoría: ${question.category.displayName}",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
=======
>>>>>>> ac753394584083abda6cebc57966da7fe7c7b46d
                    text = "Tiempo restante: $timeLeftSeconds s",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.bodyLarge
                )

                question.options.forEachIndexed { index, option ->
                    Button(
                        onClick = { onAnswerSelected(index) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isAnswerLocked
                    ) {
                        Text(text = option)
                    }
                }

                if (feedbackMessage != null) {
                    Text(
                        text = feedbackMessage,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {}
    )
}