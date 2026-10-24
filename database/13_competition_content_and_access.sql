/* HSSA competition-ready content, role allow-list, and per-course payment policy. */
\connect hssa_db;

BEGIN;

-- Administrator identity is controlled by email in the database. The user still
-- chooses their own password through normal registration; no shared password is seeded.
CREATE TABLE IF NOT EXISTS auth.admin_email_allowlist (
    email CITEXT PRIMARY KEY,
    added_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO auth.admin_email_allowlist (email)
VALUES ('sanjeevan200299@gmail.com'), ('gunasingarajah1@gmail.com')
ON CONFLICT (email) DO NOTHING;

CREATE OR REPLACE FUNCTION auth.assign_allowlisted_admin_role()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = auth, public
AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM auth.admin_email_allowlist a WHERE a.email = NEW.email) THEN
        INSERT INTO auth.user_roles (user_id, role_id, assigned_at, is_active)
        SELECT NEW.id, r.id, CURRENT_TIMESTAMP, TRUE
        FROM auth.roles r
        WHERE r.role_code = 'ADMIN'
        ON CONFLICT (user_id, role_id) DO UPDATE SET is_active = TRUE;
    END IF;
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_assign_allowlisted_admin ON auth.users;
CREATE TRIGGER trg_assign_allowlisted_admin
AFTER INSERT OR UPDATE OF email ON auth.users
FOR EACH ROW EXECUTE FUNCTION auth.assign_allowlisted_admin_role();

INSERT INTO auth.user_roles (user_id, role_id, assigned_at, is_active)
SELECT u.id, r.id, CURRENT_TIMESTAMP, TRUE
FROM auth.users u
JOIN auth.admin_email_allowlist a ON a.email = u.email
JOIN auth.roles r ON r.role_code = 'ADMIN'
ON CONFLICT (user_id, role_id) DO UPDATE SET is_active = TRUE;

ALTER TABLE lms.courses
    ADD COLUMN IF NOT EXISTS card_image_url VARCHAR(500);

CREATE TABLE IF NOT EXISTS payment.course_payment_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    course_id UUID NOT NULL UNIQUE REFERENCES lms.courses(id) ON DELETE CASCADE,
    gateway_code VARCHAR(30) NOT NULL DEFAULT 'PAYHERE',
    online_payment_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    bank_transfer_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    installments_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID REFERENCES auth.users(id) ON DELETE SET NULL,
    version INTEGER NOT NULL DEFAULT 0
);

INSERT INTO lms.course_categories (id, name, slug, description, display_order, is_active)
VALUES
('10000000-0000-0000-0000-000000000001', 'Academic English', 'academic-english', 'English for university reading, writing, and discussion.', 1, TRUE),
('10000000-0000-0000-0000-000000000002', 'Research Skills', 'research-skills', 'Ethical and practical research methods for the humanities and social sciences.', 2, TRUE),
('10000000-0000-0000-0000-000000000003', 'Literature & Culture', 'literature-culture', 'Critical reading across literature, media, and culture.', 3, TRUE),
('10000000-0000-0000-0000-000000000004', 'History & Society', 'history-society', 'Historical thinking, civic knowledge, and social analysis.', 4, TRUE),
('10000000-0000-0000-0000-000000000005', 'Digital Humanities', 'digital-humanities', 'Digital tools for cultural research and public scholarship.', 5, TRUE),
('10000000-0000-0000-0000-000000000006', 'Career Readiness', 'career-readiness', 'Communication and career skills for graduates.', 6, TRUE)
ON CONFLICT (slug) DO UPDATE SET
    name = EXCLUDED.name,
    description = EXCLUDED.description,
    display_order = EXCLUDED.display_order,
    is_active = TRUE;

INSERT INTO lms.courses (
    id, category_id, title, slug, subtitle, description, language, level, status,
    visibility, card_image_url, is_free, price, currency, discounted_price,
    duration_hours, certification_available, published_at, meta_title, meta_description
)
VALUES
('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001',
 'Academic English: Writing with Evidence', 'academic-english-writing-with-evidence',
 'Build clear arguments, responsible citation habits, and confident academic prose.',
 'A practical pathway for learners moving into English-medium higher education. Analyse model texts, plan evidence-led arguments, paraphrase responsibly, and revise for clarity.',
 'English', 'BEGINNER', 'PUBLISHED', 'PUBLIC', '/images/courses/academic-english-writing.webp', TRUE, 0, 'LKR', NULL, 18, TRUE, CURRENT_TIMESTAMP, 'Academic English: Writing with Evidence', 'Academic writing, citation, argument, and revision for humanities and social-science learners.'),
('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002',
 'Social Science Research Methods', 'social-science-research-methods',
 'Turn a meaningful question into an ethical, achievable research project.',
 'Learn how to frame research questions, review literature, select qualitative and quantitative methods, collect evidence ethically, and communicate findings.',
 'English', 'INTERMEDIATE', 'PUBLISHED', 'PUBLIC', '/images/courses/social-research-methods.webp', FALSE, 6500, 'LKR', 4900, 24, TRUE, CURRENT_TIMESTAMP, 'Social Science Research Methods', 'Applied research design and ethical evidence collection.'),
('20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003',
 'Literature and Critical Reading', 'literature-and-critical-reading',
 'Read closely, interpret confidently, and discuss texts with intellectual care.',
 'Develop close-reading strategies for fiction, poetry, essays, and cultural texts while learning to connect language, context, form, and interpretation.',
 'English', 'ALL_LEVELS', 'PUBLISHED', 'PUBLIC', '/images/courses/literature-critical-reading.webp', TRUE, 0, 'LKR', NULL, 16, TRUE, CURRENT_TIMESTAMP, 'Literature and Critical Reading', 'Critical reading and literary interpretation for all levels.'),
('20000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000004',
 'History, Society and Civic Inquiry', 'history-society-and-civic-inquiry',
 'Use sources, timelines, and competing perspectives to understand public life.',
 'Investigate how historical evidence is created and interpreted, then apply that thinking to civic questions, institutions, identity, and social change.',
 'English', 'INTERMEDIATE', 'PUBLISHED', 'PUBLIC', '/images/courses/history-society-civics.webp', FALSE, 5500, 'LKR', 4200, 20, TRUE, CURRENT_TIMESTAMP, 'History, Society and Civic Inquiry', 'Historical thinking and civic inquiry grounded in evidence.'),
('20000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000005',
 'Digital Humanities and Data Storytelling', 'digital-humanities-and-data-storytelling',
 'Transform archives and cultural data into responsible public stories.',
 'Explore digitisation, metadata, mapping, simple data analysis, and narrative visualisation through hands-on humanities projects.',
 'English', 'ADVANCED', 'PUBLISHED', 'PUBLIC', '/images/courses/digital-humanities.webp', FALSE, 8500, 'LKR', 6900, 28, TRUE, CURRENT_TIMESTAMP, 'Digital Humanities and Data Storytelling', 'Digital methods for cultural research and public communication.'),
('20000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000006',
 'Professional Communication and Career Readiness', 'professional-communication-and-career-readiness',
 'Present your knowledge clearly in interviews, meetings, and professional writing.',
 'Create a credible professional profile, practise presentations and interviews, write effective workplace documents, and communicate across teams.',
 'English', 'ALL_LEVELS', 'PUBLISHED', 'PUBLIC', '/images/courses/professional-communication.webp', FALSE, 4500, 'LKR', 3500, 14, TRUE, CURRENT_TIMESTAMP, 'Professional Communication and Career Readiness', 'Practical communication and career preparation for graduates.')
ON CONFLICT (slug) DO UPDATE SET
    category_id = EXCLUDED.category_id,
    title = EXCLUDED.title,
    subtitle = EXCLUDED.subtitle,
    description = EXCLUDED.description,
    level = EXCLUDED.level,
    status = 'PUBLISHED',
    visibility = 'PUBLIC',
    card_image_url = EXCLUDED.card_image_url,
    is_free = EXCLUDED.is_free,
    price = EXCLUDED.price,
    discounted_price = EXCLUDED.discounted_price,
    duration_hours = EXCLUDED.duration_hours,
    published_at = COALESCE(lms.courses.published_at, CURRENT_TIMESTAMP);

INSERT INTO lms.course_tags (tag_name)
VALUES ('Academic writing'), ('Evidence'), ('Research design'), ('Ethics'), ('Critical reading'),
       ('Civic inquiry'), ('Archives'), ('Data storytelling'), ('Presentation'), ('Career skills')
ON CONFLICT (tag_name) DO NOTHING;

INSERT INTO lms.course_tag_map (course_id, tag_id)
SELECT c.id, t.id
FROM (VALUES
 ('academic-english-writing-with-evidence', 'Academic writing'),
 ('academic-english-writing-with-evidence', 'Evidence'),
 ('social-science-research-methods', 'Research design'),
 ('social-science-research-methods', 'Ethics'),
 ('literature-and-critical-reading', 'Critical reading'),
 ('history-society-and-civic-inquiry', 'Civic inquiry'),
 ('digital-humanities-and-data-storytelling', 'Archives'),
 ('digital-humanities-and-data-storytelling', 'Data storytelling'),
 ('professional-communication-and-career-readiness', 'Presentation'),
 ('professional-communication-and-career-readiness', 'Career skills')
) AS x(course_slug, tag_name)
JOIN lms.courses c ON c.slug = x.course_slug
JOIN lms.course_tags t ON t.tag_name = x.tag_name
ON CONFLICT (course_id, tag_id) DO NOTHING;

INSERT INTO lms.course_outcomes (course_id, outcome_text, display_order)
SELECT c.id, x.outcome_text, x.display_order
FROM (VALUES
 ('academic-english-writing-with-evidence', 'Write a focused academic argument supported by credible evidence.', 1),
 ('academic-english-writing-with-evidence', 'Paraphrase, quote, and reference sources responsibly.', 2),
 ('social-science-research-methods', 'Design an ethical and achievable small-scale research project.', 1),
 ('social-science-research-methods', 'Choose methods that match a clear research question.', 2),
 ('literature-and-critical-reading', 'Build interpretations from language, form, and context.', 1),
 ('history-society-and-civic-inquiry', 'Evaluate primary and secondary sources critically.', 1),
 ('digital-humanities-and-data-storytelling', 'Create an evidence-based digital story from cultural data.', 1),
 ('professional-communication-and-career-readiness', 'Communicate confidently in interviews and presentations.', 1)
) AS x(course_slug, outcome_text, display_order)
JOIN lms.courses c ON c.slug = x.course_slug
WHERE NOT EXISTS (
    SELECT 1 FROM lms.course_outcomes o
    WHERE o.course_id = c.id AND o.outcome_text = x.outcome_text
);

INSERT INTO lms.modules (course_id, title, description, display_order, is_active)
SELECT c.id, x.title, x.description, x.display_order, TRUE
FROM (VALUES
 ('academic-english-writing-with-evidence', 'Academic voice and purpose', 'Audience, purpose, register, and clarity.', 1),
 ('academic-english-writing-with-evidence', 'Working with sources', 'Reading, note-making, paraphrase, and citation.', 2),
 ('social-science-research-methods', 'From topic to research question', 'Scope, feasibility, and significance.', 1),
 ('social-science-research-methods', 'Methods and research ethics', 'Sampling, interviews, surveys, observation, and consent.', 2),
 ('literature-and-critical-reading', 'How close reading works', 'Language, form, voice, and evidence.', 1),
 ('history-society-and-civic-inquiry', 'Sources, memory, and public history', 'Evidence, perspective, and contested accounts.', 1),
 ('digital-humanities-and-data-storytelling', 'Archives as data', 'Digitisation, metadata, and responsible reuse.', 1),
 ('professional-communication-and-career-readiness', 'Presenting your professional story', 'Profiles, applications, interviews, and presentations.', 1)
) AS x(course_slug, title, description, display_order)
JOIN lms.courses c ON c.slug = x.course_slug
WHERE NOT EXISTS (
    SELECT 1 FROM lms.modules m WHERE m.course_id = c.id AND m.title = x.title
);

INSERT INTO payment.course_payment_settings (
    course_id, gateway_code, online_payment_enabled, bank_transfer_enabled, installments_enabled
)
SELECT id, 'PAYHERE', NOT is_free, NOT is_free, price >= 7500
FROM lms.courses
WHERE slug IN (
 'academic-english-writing-with-evidence', 'social-science-research-methods',
 'literature-and-critical-reading', 'history-society-and-civic-inquiry',
 'digital-humanities-and-data-storytelling', 'professional-communication-and-career-readiness'
)
ON CONFLICT (course_id) DO NOTHING;

INSERT INTO event.workshops (
    id, title, slug, description, event_type, status, start_time, end_time,
    location_type, location_details, registration_deadline, timezone,
    max_participants, is_free, price
)
VALUES
('30000000-0000-0000-0000-000000000001', 'Writing a Strong Research Proposal', 'writing-a-strong-research-proposal', 'A guided clinic for refining research questions, aims, method, and feasibility.', 'WORKSHOP', 'UPCOMING', CURRENT_TIMESTAMP + INTERVAL '21 days', CURRENT_TIMESTAMP + INTERVAL '21 days 3 hours', 'HYBRID', 'HSSA Learning Studio and online', CURRENT_TIMESTAMP + INTERVAL '18 days', 'Asia/Colombo', 40, FALSE, 1500),
('30000000-0000-0000-0000-000000000002', 'Critical Reading Circle: Voice and Power', 'critical-reading-circle-voice-and-power', 'A facilitated close-reading session connecting language, identity, and public life.', 'WORKSHOP', 'UPCOMING', CURRENT_TIMESTAMP + INTERVAL '35 days', CURRENT_TIMESTAMP + INTERVAL '35 days 2 hours', 'ONLINE', 'Secure meeting link provided after registration', CURRENT_TIMESTAMP + INTERVAL '32 days', 'Asia/Colombo', 60, TRUE, 0),
('30000000-0000-0000-0000-000000000003', 'Graduate Interview Practice Lab', 'graduate-interview-practice-lab', 'Small-group interview practice with structured peer and facilitator feedback.', 'WORKSHOP', 'UPCOMING', CURRENT_TIMESTAMP + INTERVAL '49 days', CURRENT_TIMESTAMP + INTERVAL '49 days 3 hours', 'PHYSICAL', 'HSSA Learning Studio', CURRENT_TIMESTAMP + INTERVAL '45 days', 'Asia/Colombo', 24, FALSE, 1000)
ON CONFLICT (slug) DO UPDATE SET description = EXCLUDED.description, start_time = EXCLUDED.start_time, end_time = EXCLUDED.end_time, status = 'UPCOMING';

INSERT INTO event.events (
    id, title, slug, description, event_type, status, start_time, end_time,
    location_type, location_details, registration_deadline, timezone,
    max_participants, is_free, price
)
VALUES
('40000000-0000-0000-0000-000000000001', 'HSSA Open Learning Day', 'hssa-open-learning-day', 'Meet educators, preview courses, and map a personal learning pathway.', 'EVENT', 'UPCOMING', CURRENT_TIMESTAMP + INTERVAL '28 days', CURRENT_TIMESTAMP + INTERVAL '28 days 5 hours', 'HYBRID', 'HSSA Learning Studio and livestream', CURRENT_TIMESTAMP + INTERVAL '25 days', 'Asia/Colombo', 150, TRUE, 0),
('40000000-0000-0000-0000-000000000002', 'Public Humanities Forum: Culture in a Digital Age', 'public-humanities-forum-digital-age', 'Researchers and students discuss how digital platforms reshape archives, memory, and participation.', 'SEMINAR', 'UPCOMING', CURRENT_TIMESTAMP + INTERVAL '56 days', CURRENT_TIMESTAMP + INTERVAL '56 days 2 hours', 'ONLINE', 'Secure meeting link provided after registration', CURRENT_TIMESTAMP + INTERVAL '53 days', 'Asia/Colombo', 200, TRUE, 0)
ON CONFLICT (slug) DO UPDATE SET description = EXCLUDED.description, start_time = EXCLUDED.start_time, end_time = EXCLUDED.end_time, status = 'UPCOMING';

INSERT INTO research.research_categories (id, name, slug, description, display_order, is_active)
VALUES
('50000000-0000-0000-0000-000000000001', 'Education & Language', 'education-language', 'Research on language, learning, and access.', 1, TRUE),
('50000000-0000-0000-0000-000000000002', 'Culture & Society', 'culture-society', 'Research on culture, identity, institutions, and social change.', 2, TRUE)
ON CONFLICT (slug) DO UPDATE SET description = EXCLUDED.description, is_active = TRUE;

INSERT INTO research.research_projects (
    id, category_id, title, slug, abstract, content, research_type, status, published_date
)
VALUES
('51000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'Bridging the English-Medium Transition', 'bridging-the-english-medium-transition', 'An exploratory study of the academic language support learners need when entering English-medium higher education.', 'This project examines reading, writing, classroom participation, and assessment transitions to inform practical learner support.', 'REPORT', 'PUBLISHED', CURRENT_TIMESTAMP - INTERVAL '20 days'),
('51000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000002', 'Community Archives and Digital Memory', 'community-archives-and-digital-memory', 'A public-humanities study of ethical digitisation and community control of local historical materials.', 'The project brings archival practice, oral history, metadata, and community participation into one applied framework.', 'PROJECT', 'PUBLISHED', CURRENT_TIMESTAMP - INTERVAL '8 days')
ON CONFLICT (slug) DO UPDATE SET abstract = EXCLUDED.abstract, content = EXCLUDED.content, status = 'PUBLISHED';

INSERT INTO publication.publication_categories (id, name, slug, description, display_order, is_active)
VALUES
('60000000-0000-0000-0000-000000000001', 'Learning Guides', 'learning-guides', 'Practical open guides for students and educators.', 1, TRUE),
('60000000-0000-0000-0000-000000000002', 'Research Briefs', 'research-briefs', 'Accessible summaries of evidence and emerging findings.', 2, TRUE)
ON CONFLICT (slug) DO UPDATE SET description = EXCLUDED.description, is_active = TRUE;

INSERT INTO publication.publications (
    id, category_id, title, slug, abstract, content, publication_type, status, published_date
)
VALUES
('61000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', 'A Student Guide to Reading Academic Articles', 'student-guide-reading-academic-articles', 'A practical sequence for previewing, questioning, annotating, and reviewing academic articles.', 'Use a staged reading process: establish purpose, inspect structure, identify the central claim, trace the evidence, and record questions.', 'REPORT', 'PUBLISHED', CURRENT_TIMESTAMP - INTERVAL '12 days'),
('61000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000002', 'Language Confidence and Participation', 'language-confidence-and-participation', 'A concise evidence brief on creating safer conditions for multilingual classroom participation.', 'This brief translates current evidence into practical recommendations for learners, tutors, and course designers.', 'WORKING_PAPER', 'PUBLISHED', CURRENT_TIMESTAMP - INTERVAL '4 days')
ON CONFLICT (slug) DO UPDATE SET abstract = EXCLUDED.abstract, content = EXCLUDED.content, status = 'PUBLISHED';

INSERT INTO lms.book_categories (id, name, description)
VALUES
('70000000-0000-0000-0000-000000000001', 'Literature', 'Fiction, poetry, drama, and literary essays.'),
('70000000-0000-0000-0000-000000000002', 'Social Thought', 'Accessible works in society, culture, and public life.')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description;

INSERT INTO lms.books (id, category_id, title, author, language, description, is_featured, is_active)
VALUES
('71000000-0000-0000-0000-000000000001', '70000000-0000-0000-0000-000000000001', 'Selected South Asian Short Fiction', 'HSSA Reading Circle', 'English', 'A curated reading pathway for discussing voice, place, memory, and social change.', TRUE, TRUE),
('71000000-0000-0000-0000-000000000002', '70000000-0000-0000-0000-000000000002', 'Ways of Seeing Society', 'HSSA Learning Collective', 'English', 'Short accessible readings that introduce evidence, institutions, inequality, and everyday social life.', TRUE, TRUE),
('71000000-0000-0000-0000-000000000003', '70000000-0000-0000-0000-000000000001', 'Poetry, Language and Belonging', 'HSSA Reading Circle', 'English', 'A multilingual-friendly selection for close reading and reflective discussion.', FALSE, TRUE)
ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description, is_active = TRUE;

INSERT INTO cms.contact_offices (
    id, office_name, address, phone, email, opening_hours, is_head_office, is_active
)
VALUES
('80000000-0000-0000-0000-000000000001', 'HSSA Learning Studio', 'Sri Lanka — visits by appointment', '+94 11 000 0000', 'hello@hssa.lk', 'Monday–Friday, 9:00–17:00', TRUE, TRUE)
ON CONFLICT (id) DO UPDATE SET opening_hours = EXCLUDED.opening_hours, is_active = TRUE;

COMMIT;

SELECT 'HSSA competition content and access rules installed' AS result;
