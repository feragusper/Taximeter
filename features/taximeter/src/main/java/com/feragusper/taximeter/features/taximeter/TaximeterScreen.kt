package com.feragusper.taximeter.features.taximeter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feragusper.taximeter.libraries.design.ui.theme.TaximeterTheme
import kotlinx.coroutines.launch
import java.util.UUID

internal object TestTag {
    const val DISTANCE_LABEL = "distance_label"
    const val DISTANCE_VALUE = "distance_value"
    const val TOTAL_FARE_LABEL = "total_fare_label"
    const val TOTAL_FARE_VALUE = "total_fare_value"
    const val TAXIMETER_TITLE = "taximeter_title"
    const val START_RIDE = "start_ride"
    const val FINISH_RIDE = "finish_ride"
}

@Composable
internal fun Taximeter(
    viewModel: TaximeterViewModel,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorSnackBar: (String) -> Unit = { errorMessage ->
        scope.launch {
            snackBarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short,
            )
        }
    }

    viewModel.sideEffect.observeWithLifecycle { sideEffect ->
        when (sideEffect) {
            is TaximeterSideEffect.ShowError -> errorSnackBar("Error!")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onUserIntent(TaximeterIntent.Open)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.testTag(TestTag.TAXIMETER_TITLE),
                    text = stringResource(R.string.features_taximeter_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    actionColor = MaterialTheme.colorScheme.onError,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedVisibility(
                visible = viewState.isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                TaximeterShimmer()
            }

            AnimatedVisibility(
                visible = !viewState.isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                TaximeterContent(viewState) {
                    viewModel.onUserIntent(it)
                }
            }
        }
    }
}

@Composable
fun Modifier.withShimmerEffect(): Modifier {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer_animation")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,  // Start way off-screen to the left
        targetValue = 1000f,    // Move far to the right
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300, // Adjust speed of shimmer
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_translation"
    )

    return this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            end = Offset(translateAnim + 800f, 0f)  // Shimmer width
        )
    )
}

@Composable
private fun TaximeterShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerCard(modifier = Modifier.weight(1f))
            ShimmerCard(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        ShimmerCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        repeat(3) {
            ShimmerCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun ShimmerCard(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .withShimmerEffect()
    )
}

@Composable
internal fun TaximeterContent(
    viewState: TaximeterViewState,
    onUserIntent: (TaximeterIntent) -> Unit
) {
    var selectedSupplements by remember { mutableStateOf(viewState.supplements) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeCard(
                    modifier = Modifier.weight(1f),
                    elapsedTime = viewState.elapsedTime
                )
                DistanceCard(
                    modifier = Modifier.weight(1f),
                    distance = viewState.distance
                )
            }
            TotalFareCard(viewState.fare)
        }

        Spacer(modifier = Modifier.height(16.dp))

        fun supplementsIdsToCharge() = buildList {
            selectedSupplements.forEach { selectedSupplement ->
                repeat(selectedSupplement.count) { add(selectedSupplement.id) }
            }
        }

        Text(
            text = stringResource(R.string.features_taximeter_supplements),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DynamicSupplementsSection(
            supplements = selectedSupplements,
            onSupplementChange = { updatedSupplements ->
                selectedSupplements = updatedSupplements
                if (viewState.inProgress) {
                    onUserIntent(TaximeterIntent.UpdateSupplements(supplementsIdsToCharge()))
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = viewState.priceBreakdown != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            viewState.priceBreakdown?.let {
                PriceBreakdown(it)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onUserIntent(
                    if (!viewState.inProgress) {
                        TaximeterIntent.StartRide(supplementsIdsToCharge())
                    } else {
                        TaximeterIntent.StopRide
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (!viewState.inProgress) {
                Text(
                    modifier = Modifier.testTag(TestTag.START_RIDE),
                    text = stringResource(R.string.features_taximeter_start_ride),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Text(
                    modifier = Modifier.testTag(TestTag.FINISH_RIDE),
                    text = stringResource(R.string.features_taximeter_finish_ride),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun TimeCard(modifier: Modifier = Modifier, elapsedTime: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.features_taximeter_time),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = elapsedTime,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DistanceCard(modifier: Modifier = Modifier, distance: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.features_taximeter_distance),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.testTag(TestTag.DISTANCE_LABEL)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = distance,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag(TestTag.DISTANCE_VALUE)
            )
        }
    }
}

@Composable
private fun TotalFareCard(fare: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.features_taximeter_total_fare),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.testTag(TestTag.TOTAL_FARE_LABEL)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = fare,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag(TestTag.TOTAL_FARE_VALUE)
            )
        }
    }
}


@Composable
private fun DynamicSupplementsSection(
    supplements: List<TaximeterViewState.Supplement>,
    onSupplementChange: (List<TaximeterViewState.Supplement>) -> Unit
) {
    Column {
        supplements.forEach { supplement ->
            SupplementRow(
                name = supplement.name,
                count = supplement.count,
                onChange = { change ->
                    val updatedSupplements = supplements.map {
                        if (it.id == supplement.id) it.copy(count = it.count + change)
                        else it
                    }
                    onSupplementChange(updatedSupplements)
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SupplementRow(
    name: String,
    count: Int,
    onChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onChange(-1) }, enabled = count > 0) {
                Text("-")
            }
            Text(
                text = "$count",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Button(onClick = { onChange(1) }) {
                Text("+")
            }
        }
    }
}

@Composable
private fun PriceBreakdown(priceBreakdown: TaximeterViewState.PriceBreakdown) {
    Column {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), // Customize color
            thickness = 1.dp,  // Customize thickness
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.features_taximeter_price_breakdown),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        priceBreakdown.items.forEach { (concept, price) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = concept, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.features_taximeter_total),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = priceBreakdown.total,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private val testViewState = TaximeterViewState(
    fare = "€40.05",
    elapsedTime = "5:30",
    distance = "5.5 km",
    supplements = listOf(
        TaximeterViewState.Supplement(UUID.randomUUID(), "Baby Seat", 1),
        TaximeterViewState.Supplement(UUID.randomUUID(), "Extra Baggage", 2)
    ),
    priceBreakdown = TaximeterViewState.PriceBreakdown(
        items = listOf(
            TaximeterViewState.PriceBreakdown.Item("Base Fare", "€10.00"),
            TaximeterViewState.PriceBreakdown.Item("Time Fare", "€15.00"),
        ),
        total = "€40.05"
    )
)

@Preview(showBackground = true, name = "Ride In Progress")
@Composable
private fun TaximeterLoadingPreview() {
    TaximeterTheme {
        TaximeterContent(
            viewState = testViewState.copy(inProgress = true),
            onUserIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Ride In Progress")
@Composable
private fun TaximeterInProgressPreview() {
    TaximeterTheme {
        TaximeterContent(
            viewState = testViewState.copy(inProgress = true, priceBreakdown = null),
            onUserIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Ride Completed")
@Composable
private fun TaximeterCompletedPreview() {
    TaximeterTheme {
        TaximeterContent(
            viewState = testViewState.copy(inProgress = false, fare = "€50.00"),
            onUserIntent = {}
        )
    }
}