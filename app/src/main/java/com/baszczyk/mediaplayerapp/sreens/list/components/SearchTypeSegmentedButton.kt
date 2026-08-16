package com.baszczyk.mediaplayerapp.sreens.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SearchTypeSegmentedButton(
    selectedSort: Int,
    onSortSelected: (Int) -> Unit
) {

    val labels = listOf("Tytuły", "Autorzy")

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, label ->
            SegmentedButton(
                selected = selectedSort == index,
                onClick = { onSortSelected(index) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = labels.size
                )

            ) {
                Text(
                    text = label,
                    fontSize = 12.sp
                )
            }
        }
    }
}
