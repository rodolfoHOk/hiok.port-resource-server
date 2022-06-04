CREATE TABLE feedback_feedbacks (
	id uuid NOT NULL,
	comment text NOT NULL,
	created_at timestamp NOT NULL,
	has_screenshot boolean NOT NULL,
	modified_at timestamp NULL,
	status varchar(20) NOT NULL,
	type varchar(20) NOT NULL,
	CONSTRAINT feedback_feedbacks_pk PRIMARY KEY (id)
);

CREATE TABLE feedback_screenshots (
	feedback_id uuid NOT NULL,
	content_type varchar(30) NOT NULL,
	filename varchar(150) NOT NULL,
	size bigint NOT NULL,
	CONSTRAINT feedback_screenshot_pk PRIMARY KEY (feedback_id)
);
