-- 데이터베이스 초기화
DROP DATABASE IF EXISTS ssafit;
CREATE DATABASE IF NOT EXISTS ssafit;
USE ssafit;

-- 1) 회원 테이블
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    email      VARCHAR(100)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    nickname   VARCHAR(30)  NOT NULL,
    age        INT          NOT NULL,
    role       VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 2) 영상 테이블
CREATE TABLE IF NOT EXISTS videos (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    part        VARCHAR(10) NOT NULL,
    url         VARCHAR(500) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_videos_user (user_id),
    CONSTRAINT fk_videos_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 3) 찜(picks) 테이블
CREATE TABLE IF NOT EXISTS favorites (
    id       BIGINT NOT NULL AUTO_INCREMENT,
    user_id  BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_picks_user_video (user_id, video_id),
    INDEX idx_picks_user  (user_id),
    INDEX idx_picks_video (video_id),
    CONSTRAINT fk_picks_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_picks_video
        FOREIGN KEY (video_id)
        REFERENCES videos(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 4) 영상 조회 로그 테이블
CREATE TABLE IF NOT EXISTS video_viewlogs (
    id        BIGINT NOT NULL AUTO_INCREMENT,
    user_id   BIGINT NOT NULL,
    video_id  BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_vv_user_video (user_id, video_id),
    INDEX idx_vv_user  (user_id),
    INDEX idx_vv_video (video_id),
    CONSTRAINT fk_vv_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_vv_video
        FOREIGN KEY (video_id)
        REFERENCES videos(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 5) 리뷰 테이블
CREATE TABLE IF NOT EXISTS reviews (
    id       BIGINT NOT NULL AUTO_INCREMENT,
    user_id  BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    title    VARCHAR(100) NOT NULL,
    content  TEXT    NOT NULL,
    rating   INT     NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_reviews_user (user_id),
    INDEX idx_reviews_video (video_id),
    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_reviews_video
        FOREIGN KEY (video_id)
        REFERENCES videos(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 6) 리뷰 조회 로그 테이블
CREATE TABLE IF NOT EXISTS reviews_viewlogs (
    id        BIGINT NOT NULL AUTO_INCREMENT,
    user_id   BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_rv_user_review (user_id, review_id),
    INDEX idx_rv_user   (user_id),
    INDEX idx_rv_review (review_id),
    CONSTRAINT fk_rv_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_rv_review
        FOREIGN KEY (review_id)
        REFERENCES reviews(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;

-- 7) 댓글 테이블
CREATE TABLE IF NOT EXISTS comments (
    id        BIGINT NOT NULL AUTO_INCREMENT,
    user_id   BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    content   TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_comments_user (user_id),
    INDEX idx_comments_review (review_id),
    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_comments_review
        FOREIGN KEY (review_id)
        REFERENCES reviews(id)
    ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;



-- 더미 데이터 (유저는 회원가입 시켜야함)

-- 비디오
INSERT INTO videos (id, user_id, title, description, part, url, created_at, modified_at)
VALUES (1, 1, '영상1', '설명주저리주저리', 'ARM', 'http://www.ssafit.com', now(), now());

INSERT INTO videos (id, user_id, title, description, part, url, created_at, modified_at)
VALUES (2, 1, '영상1', '설명주저리주저리', 'ARM', 'http://www.ssafit.com', now(), now());

INSERT INTO videos (id, user_id, title, description, part, url, created_at, modified_at)
VALUES (3, 1, '영상1', '설명주저리주저리', 'ARM', 'http://www.ssafit.com', now(), now());

INSERT INTO videos (id, user_id, title, description, part, url, created_at, modified_at)
VALUES (4, 1, '영상1', '설명주저리주저리', 'ARM', 'http://www.ssafit.com', now(), now());

INSERT INTO videos (id, user_id, title, description, part, url, created_at, modified_at)
VALUES (5, 1, '영상1', '설명주저리주저리', 'ARM', 'http://www.ssafit.com', now(), now());


-- 리뷰
INSERT INTO reviews (id, user_id, video_id, title, content, rating, created_at, modified_at)
VALUES (1, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, now(), now());

INSERT INTO reviews (id, user_id, video_id, title, content, rating, created_at, modified_at)
VALUES (2, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, now(), now());

INSERT INTO reviews (id, user_id, video_id, title, content, rating, created_at, modified_at)
VALUES (3, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, now(), now());


