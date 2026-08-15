package com.baszczyk.mediaplayerapp.sreens.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterChips(
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val filtersList = listOf(
            "Wszystkie",
            "Ulubione",
            "Odsłuchane"
        )

        filtersList.forEachIndexed { index, label ->
            CustomFilterChip(
                id = index,
                label = label,
                selectedFilter = selectedFilter,
                onFilterSelected = { onFilterSelected(index) }
            )
        }
    }
}

@Composable
private fun CustomFilterChip(
    id: Int,
    label: String,
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    FilterChip(
        selected = selectedFilter == id,
        onClick = {
            onFilterSelected(id)
        },
        label = {
            Text(
                text = label,
                fontSize = 12.sp
            )
        }
    )
}