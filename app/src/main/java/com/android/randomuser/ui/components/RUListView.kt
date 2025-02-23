package com.android.randomuser.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> RUListView(
    modifier: Modifier = Modifier,
    gridState: LazyGridState,
    gridContent: @Composable (item: T, index: Int) -> Unit,
    onFetchNextPage: () -> Unit,
    list: List<T?>,
    emptyGridContent: @Composable () -> Unit = { RUEmptyScreen() },
    showBottomLoading: Boolean = false,
    isLoading: Boolean,
    columns: Int = 2,
    contentPadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
) {
    val atLastIndex by remember {
        derivedStateOf {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == gridState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(atLastIndex) {
        if (atLastIndex) onFetchNextPage()
    }

    Box(modifier = modifier) {
        when {
            list.isValidList() -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(columns),
                    contentPadding = contentPadding,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(list) { index, it ->
                        if (it != null) {
                            gridContent(it, index)
                        }
                    }

                    if (showBottomLoading) {
                        item(span = { GridItemSpan(columns) }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RULoadingIndicator()
                            }
                        }
                    }
                }
            }

            !list.isValidList() && !isLoading -> {
                emptyGridContent()
            }
        }
    }
}

fun <T> List<T>.isValidList(): Boolean = this.isNotEmpty()
