package com.xpeho.xpeapp.data.model.qvst

/*
{
        "question_id": "4",
        "question": "Comment te sens-tu dans l'entreprise ?",
        "hasAnswered": false,
        "answers": [
            {
                "id": "1",
                "answer": "Tout à fait",
                "value": "5"
            },
            {
                "id": "2",
                "answer": "Plutôt oui",
                "value": "4"
            },
            {
                "id": "3",
                "answer": "Cela dépend",
                "value": "3"
            },
            {
                "id": "4",
                "answer": "Plutôt non",
                "value": "2"
            },
            {
                "id": "5",
                "answer": "Pas du tout",
                "value": "1"
            }
        ]
    }
 */

data class QvstQuestion(
    val question_id: String,
    val question: String,
    val hasAnswered: Boolean,
    val answers: List<QvstAnswer>,
    var userAnswer: String? = null,
)
