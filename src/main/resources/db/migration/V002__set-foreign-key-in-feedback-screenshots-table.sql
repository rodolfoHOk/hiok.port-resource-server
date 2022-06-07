ALTER TABLE feedback_screenshots
ADD CONSTRAINT screenshot_feedbacks_fk
FOREIGN KEY (feedback_id) REFERENCES feedback_feedbacks(id);