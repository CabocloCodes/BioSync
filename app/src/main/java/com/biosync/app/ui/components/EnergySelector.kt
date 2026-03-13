package com.biosync.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.biosync.app.data.model.EnergyLevel
import com.biosync.app.ui.theme.Blue40
import com.biosync.app.ui.theme.Blue60
import com.biosync.app.ui.theme.DarkSurfaceVariant
import com.biosync.app.ui.theme.EnergyHigh
import com.biosync.app.ui.theme.EnergyLow
import com.biosync.app.ui.theme.EnergyMedium
import com.biosync.app.ui.theme.TextSecondary
import com.biosync.app.ui.theme.Violet40
import com.biosync.app.ui.theme.Violet60

@Composable
fun EnergySelector(
    onEnergySelected: (EnergyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Como está sua energia agora?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecione seu nível de energia para receber sugestões personalizadas",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        EnergyOption(
            energyLevel = EnergyLevel.LOW,
            icon = Icons.Filled.Battery3Bar,
            title = "Energia Baixa",
            subtitle = "Tarefas leves e rápidas",
            color = EnergyLow,
            gradientColors = listOf(EnergyLow.copy(alpha = 0.1f), EnergyLow.copy(alpha = 0.05f)),
            onClick = { onEnergySelected(EnergyLevel.LOW) }
        )

        EnergyOption(
            energyLevel = EnergyLevel.MEDIUM,
            icon = Icons.Filled.BatteryChargingFull,
            title = "Energia Média",
            subtitle = "Tarefas moderadas e produtivas",
            color = EnergyMedium,
            gradientColors = listOf(Blue40.copy(alpha = 0.15f), Blue60.copy(alpha = 0.05f)),
            onClick = { onEnergySelected(EnergyLevel.MEDIUM) }
        )

        EnergyOption(
            energyLevel = EnergyLevel.HIGH,
            icon = Icons.Filled.BatteryFull,
            title = "Energia Alta",
            subtitle = "Pronto para desafios complexos",
            color = EnergyHigh,
            gradientColors = listOf(Violet40.copy(alpha = 0.15f), Violet60.copy(alpha = 0.05f)),
            onClick = { onEnergySelected(EnergyLevel.HIGH) }
        )
    }
}

@Composable
private fun EnergyOption(
    energyLevel: EnergyLevel,
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    gradientColors: List<androidx.compose.ui.graphics.Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(gradientColors)
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = color
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
