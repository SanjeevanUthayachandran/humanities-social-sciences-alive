package com.hssa.contact.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.contact.dto.request.ContactRequest;
import com.hssa.contact.dto.request.ReplyRequest;
import com.hssa.contact.dto.response.ContactResponse;
import com.hssa.contact.dto.response.ContactStatisticsResponse;
import com.hssa.contact.entity.ContactMessage;
import com.hssa.contact.enums.MessageStatus;
import com.hssa.contact.event.ContactSubmittedEvent;
import com.hssa.contact.event.SupportTicketAssignedEvent;
import com.hssa.contact.event.SupportTicketResolvedEvent;
import com.hssa.contact.repository.ContactMessageRepository;
import com.hssa.contact.service.ContactService;
import com.hssa.contact.specification.ContactSpecification;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.notification.dto.request.CreateNotificationRequest;
import com.hssa.notification.enums.NotificationType;
import com.hssa.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ContactResponse submitContactMessage(ContactRequest request, String ipAddress, String userAgent) {
        ContactMessage message = ContactMessage.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .subject(request.getSubject())
                .inquiryType(request.getInquiryType())
                .message(request.getMessage())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status(MessageStatus.NEW)
                .priority("NORMAL")
                .build();
        message = contactMessageRepository.save(message);
        eventPublisher.publishEvent(new ContactSubmittedEvent(this, message.getId()));
        return buildResponse(message);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getMessage(UUID id) {
        return buildResponse(getActive(id));
    }

    @Override
    @Transactional
    public void assignMessage(UUID id, UUID assigneeId, UUID assignerId) {
        ContactMessage message = getActive(id);
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        
        message.setAssignedTo(assignee);
        message.setAssignedAt(LocalDateTime.now());
        if (message.getStatus() == MessageStatus.NEW) {
            message.setStatus(MessageStatus.IN_PROGRESS);
        }
        contactMessageRepository.save(message);
        eventPublisher.publishEvent(new SupportTicketAssignedEvent(this, id));
        
        // Notify Assignee
        CreateNotificationRequest notif = new CreateNotificationRequest();
        notif.setRecipientId(assigneeId);
        notif.setNotificationType(NotificationType.SYSTEM);
        notif.setTitle("Ticket Assigned");
        notif.setMessage("Ticket #" + id + " has been assigned to you.");
        notificationService.sendNotification(notif, assignerId);
    }

    @Override
    @Transactional
    public void resolveMessage(UUID id, UUID resolverId) {
        ContactMessage message = getActive(id);
        message.setStatus(MessageStatus.RESOLVED);
        message.setResolvedBy(resolverId);
        message.setClosedAt(LocalDateTime.now());
        contactMessageRepository.save(message);
        eventPublisher.publishEvent(new SupportTicketResolvedEvent(this, id));
    }

    @Override
    @Transactional
    public void replyToMessage(UUID id, ReplyRequest request, UUID replierId) {
        ContactMessage message = getActive(id);
        User replier = userRepository.findById(replierId)
                .orElseThrow(() -> new ResourceNotFoundException("Replier not found"));

        message.setRepliedBy(replier);
        message.setReplyMessage(request.getReplyMessage());
        message.setRepliedAt(LocalDateTime.now());
        if (message.getStatus() == MessageStatus.NEW) {
            message.setStatus(MessageStatus.IN_PROGRESS);
        }
        contactMessageRepository.save(message);

        // Notify user via Email by resolving user via email if they have an account
        userRepository.findByEmail(message.getEmail()).ifPresent(user -> {
            CreateNotificationRequest notif = new CreateNotificationRequest();
            notif.setRecipientId(user.getId());
            notif.setNotificationType(NotificationType.SYSTEM);
            notif.setTitle("Reply to your inquiry");
            notif.setMessage(request.getReplyMessage());
            notificationService.sendNotification(notif, replierId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponse> searchMessages(String status, String priority, String inquiryType, String keyword, Pageable pageable) {
        return contactMessageRepository.findAll(ContactSpecification.filterBy(status, priority, inquiryType, keyword), pageable)
                .map(this::buildResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactStatisticsResponse getStatistics() {
        long total = contactMessageRepository.count();
        long neu = contactMessageRepository.count(ContactSpecification.filterBy("NEW", null, null, null));
        long inProgress = contactMessageRepository.count(ContactSpecification.filterBy("IN_PROGRESS", null, null, null));
        long resolved = contactMessageRepository.count(ContactSpecification.filterBy("RESOLVED", null, null, null));
        long closed = contactMessageRepository.count(ContactSpecification.filterBy("CLOSED", null, null, null));

        return ContactStatisticsResponse.builder()
                .totalMessages(total)
                .newMessages(neu)
                .inProgressMessages(inProgress)
                .resolvedMessages(resolved)
                .closedMessages(closed)
                .build();
    }

    private ContactMessage getActive(UUID id) {
        return contactMessageRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found"));
    }

    private ContactResponse buildResponse(ContactMessage m) {
        return ContactResponse.builder()
                .id(m.getId())
                .fullName(m.getFullName())
                .email(m.getEmail())
                .phoneNumber(m.getPhoneNumber())
                .subject(m.getSubject())
                .inquiryType(m.getInquiryType())
                .message(m.getMessage())
                .status(m.getStatus().name())
                .priority(m.getPriority())
                .assignedToName(m.getAssignedTo() != null ? m.getAssignedTo().getFirstName() + " " + m.getAssignedTo().getLastName() : null)
                .resolvedByName(m.getResolvedBy() != null ? m.getResolvedBy().toString() : null)
                .replyMessage(m.getReplyMessage())
                .createdAt(m.getCreatedAt())
                .build();
    }
}