/*
==============================================================
Project : Humanities & Social Sciences Alive (HSSA)
Database: PostgreSQL
File    : 04_15_notifications.sql
Version : 1.0
==============================================================
Description:
Notification Management Module
==============================================================
*/

\connect hssa_db;

SET search_path TO notification, public;

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------

CREATE TABLE notification.notifications
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    sender_id UUID,

    recipient_id UUID NOT NULL,

    notification_type notification.notification_type
            NOT NULL,

    title VARCHAR(255) NOT NULL,

    message TEXT NOT NULL,

    action_url TEXT,

    image_media_id UUID,

    status notification.notification_status
            NOT NULL
            DEFAULT 'UNREAD',

    is_email_sent BOOLEAN DEFAULT FALSE,

    is_push_sent BOOLEAN DEFAULT FALSE,

    is_sms_sent BOOLEAN DEFAULT FALSE,

    expires_at TIMESTAMPTZ,

    read_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_notification_sender
        FOREIGN KEY(sender_id)
        REFERENCES auth.users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_notification_receiver
        FOREIGN KEY(recipient_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- NOTIFICATION TEMPLATES
------------------------------------------------------------

CREATE TABLE notification.notification_templates
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    template_code VARCHAR(100) UNIQUE NOT NULL,

    title_template VARCHAR(255) NOT NULL,

    message_template TEXT NOT NULL,

    notification_type notification.notification_type
            NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- USER NOTIFICATION SETTINGS
------------------------------------------------------------

CREATE TABLE notification.user_notification_settings
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL UNIQUE,

    email_enabled BOOLEAN DEFAULT TRUE,

    push_enabled BOOLEAN DEFAULT TRUE,

    sms_enabled BOOLEAN DEFAULT FALSE,

    course_notifications BOOLEAN DEFAULT TRUE,

    payment_notifications BOOLEAN DEFAULT TRUE,

    certificate_notifications BOOLEAN DEFAULT TRUE,

    workshop_notifications BOOLEAN DEFAULT TRUE,

    event_notifications BOOLEAN DEFAULT TRUE,

    news_notifications BOOLEAN DEFAULT TRUE,

    announcement_notifications BOOLEAN DEFAULT TRUE,

    system_notifications BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_settings_user
        FOREIGN KEY(user_id)
        REFERENCES auth.users(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- NOTIFICATION LOGS
------------------------------------------------------------

CREATE TABLE notification.notification_logs
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    notification_id UUID NOT NULL,

    delivery_channel VARCHAR(30),

    delivery_status VARCHAR(30),

    sent_at TIMESTAMPTZ,

    error_message TEXT,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_log
        FOREIGN KEY(notification_id)
        REFERENCES notification.notifications(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- INDEXES
------------------------------------------------------------

CREATE INDEX idx_notifications_recipient
ON notification.notifications(recipient_id);

CREATE INDEX idx_notifications_sender
ON notification.notifications(sender_id);

CREATE INDEX idx_notifications_status
ON notification.notifications(status);

CREATE INDEX idx_notifications_type
ON notification.notifications(notification_type);

CREATE INDEX idx_notifications_created
ON notification.notifications(created_at);

CREATE INDEX idx_notification_logs
ON notification.notification_logs(notification_id);

------------------------------------------------------------
-- DEFAULT TEMPLATES
------------------------------------------------------------

INSERT INTO notification.notification_templates
(template_code,title_template,message_template,notification_type)

VALUES

(
'WELCOME',

'Welcome to HSSA',

'Welcome to Humanities & Social Sciences Alive.',

'SYSTEM'
),

(
'COURSE_ENROLLED',

'Course Enrollment',

'You have successfully enrolled in a course.',

'COURSE'
),

(
'PAYMENT_SUCCESS',

'Payment Successful',

'Your payment has been successfully completed.',

'PAYMENT'
),

(
'CERTIFICATE_READY',

'Certificate Ready',

'Your course certificate is ready for download.',

'CERTIFICATE'
),

(
'WORKSHOP_REGISTERED',

'Workshop Registration',

'You have successfully registered for the workshop.',

'WORKSHOP'
),

(
'EVENT_REGISTERED',

'Event Registration',

'You have successfully registered for the event.',

'EVENT'
);

------------------------------------------------------------
-- COMMENTS
------------------------------------------------------------

COMMENT ON TABLE notification.notifications IS
'Stores notifications sent to users.';

COMMENT ON TABLE notification.notification_templates IS
'Notification templates used throughout the platform.';

COMMENT ON TABLE notification.user_notification_settings IS
'Stores notification preferences for each user.';

COMMENT ON TABLE notification.notification_logs IS
'Delivery log for notification channels.';

------------------------------------------------------------
-- VERIFY
------------------------------------------------------------

SELECT
table_name
FROM information_schema.tables
WHERE table_schema='notification'
ORDER BY table_name;

------------------------------------------------------------
-- END OF FILE
------------------------------------------------------------