package dev.cammiescorner.devotion.neoforge.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber
public class ClientEventBus {
	@SubscribeEvent
	public void registerShaders(RegisterShadersEvent event) {

	}
}
