package com.tpg.ratelimitednotifications;

import com.tpg.ratelimitednotifications.controller.rest.NotificationController;
import com.tpg.ratelimitednotifications.entity.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

	private static final Logger logger = Logger.getLogger(NotificationControllerTest.class.getName());

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	private NotificationController notificationController;

	@BeforeEach
	void setUp() {
		logger.setUseParentHandlers(false);
		logger.addHandler(new ConsoleHandler() {{setOutputStream(System.out);}});
		logger.info("Setting up test environment");
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		notificationController = new NotificationController();
		notificationController.redisTemplate = redisTemplate;
	}

	@Test
	void testSendNotificationStatusWithinLimit() {
		logger.info("Starting testSendNotificationStatusWithinLimit");
		String recipient = "user@example.com";
		String notificationType = "Status";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(1);

		String result = notificationController.sendNotification(request);

		logger.info("Verifying results for testSendNotificationStatusWithinLimit");
		assertEquals("Notification sent successfully", result);
		verify(valueOperations).increment(recipient + "=" + notificationType, 1);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(redisTemplate).expire(recipient + "=" + notificationType, 60L, TimeUnit.SECONDS);
		logger.info("testSendNotificationStatusWithinLimit completed successfully");
	}

	@Test
	void testSendNotificationStatusExceedingLimit() {
		logger.info("Starting testSendNotificationStatusExceedingLimit");
		String recipient = "user@example.com";
		String notificationType = "Status";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(2);

		String result = notificationController.sendNotification(request);
		String expectedErrorMessage = "Error: Rate limit exceeded for Status notifications";

		logger.info("Verifying results for testSendNotificationStatusExceedingLimit");
		assertEquals(expectedErrorMessage, result);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(valueOperations, never()).increment(anyString(), anyInt());
		verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
		logger.info("testSendNotificationStatusExceedingLimit completed successfully");
	}

	@Test
	void testSendNotificationNewsWithinLimit() {
		logger.info("Starting testSendNotificationNewsWithinLimit");
		String recipient = "user@example.com";
		String notificationType = "News";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(0);

		String result = notificationController.sendNotification(request);

		logger.info("Verifying results for testSendNotificationNewsWithinLimit");
		assertEquals("Notification sent successfully", result);
		verify(valueOperations).increment(recipient + "=" + notificationType, 1);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(redisTemplate).expire(recipient + "=" + notificationType, 86400L, TimeUnit.SECONDS);
		logger.info("testSendNotificationNewsWithinLimit completed successfully");
	}

	@Test
	void testSendNotificationNewsExceedingLimit() {
		logger.info("Starting testSendNotificationNewsExceedingLimit");
		String recipient = "user@example.com";
		String notificationType = "News";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(1);

		String result = notificationController.sendNotification(request);
		String expectedErrorMessage = "Error: Rate limit exceeded for News notifications";

		logger.info("Verifying results for testSendNotificationNewsExceedingLimit");
		assertEquals(expectedErrorMessage, result);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(valueOperations, never()).increment(anyString(), anyInt());
		verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
		logger.info("testSendNotificationNewsExceedingLimit completed successfully");
	}

	@Test
	void testSendNotificationMarketingWithinLimit() {
		logger.info("Starting testSendNotificationMarketingWithinLimit");
		String recipient = "user@example.com";
		String notificationType = "Marketing";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(2);

		String result = notificationController.sendNotification(request);

		logger.info("Verifying results for testSendNotificationMarketingWithinLimit");
		assertEquals("Notification sent successfully", result);
		verify(valueOperations).increment(recipient + "=" + notificationType, 1);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(redisTemplate).expire(recipient + "=" + notificationType, 3600L, TimeUnit.SECONDS);
		logger.info("testSendNotificationMarketingWithinLimit completed successfully");
	}

	@Test
	void testSendNotificationMarketingExceedingLimit() {
		logger.info("Starting testSendNotificationMarketingExceedingLimit");
		String recipient = "user@example.com";
		String notificationType = "Marketing";
		NotificationRequest request = new NotificationRequest(recipient, notificationType);
		when(valueOperations.get(recipient + "=" + notificationType)).thenReturn(3);

		String result = notificationController.sendNotification(request);
		String expectedErrorMessage = "Error: Rate limit exceeded for Marketing notifications";

		logger.info("Verifying results for testSendNotificationMarketingExceedingLimit");
		assertEquals(expectedErrorMessage, result);
		verify(valueOperations).get(recipient + "=" + notificationType);
		verify(valueOperations, never()).increment(anyString(), anyInt());
		verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
		logger.info("testSendNotificationMarketingExceedingLimit completed successfully");
	}
}