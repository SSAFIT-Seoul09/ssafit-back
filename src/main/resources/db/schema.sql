-- 기존 DB 삭제 및 생성
DROP DATABASE IF EXISTS ssafit;
CREATE DATABASE IF NOT EXISTS ssafit;
USE ssafit;

-- 1) 회원 테이블
CREATE TABLE IF NOT EXISTS users (
                                     id         BIGINT       NOT NULL AUTO_INCREMENT,
                                     email      VARCHAR(100) NOT NULL UNIQUE,
                                     password   VARCHAR(255) NOT NULL,
                                     nickname   VARCHAR(30)  NOT NULL,
                                     age        INT          NOT NULL,
                                     role       VARCHAR(20)  NOT NULL,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (id),
                                     UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) 영상 테이블
CREATE TABLE IF NOT EXISTS videos (
                                      id          BIGINT       NOT NULL AUTO_INCREMENT,
                                      user_id     BIGINT       NOT NULL,
                                      title       VARCHAR(100) NOT NULL,
                                      description TEXT,
                                      part        VARCHAR(10)  NOT NULL,
                                      url         VARCHAR(500) NOT NULL,
                                      views       INT UNSIGNED DEFAULT 0,
                                      created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (id),
                                      INDEX idx_videos_user (user_id),
                                      CONSTRAINT fk_videos_user FOREIGN KEY (user_id) REFERENCES users(id)
                                          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) 찜 테이블
CREATE TABLE IF NOT EXISTS favorites (
                                         id        BIGINT NOT NULL AUTO_INCREMENT,
                                         user_id   BIGINT NOT NULL,
                                         video_id  BIGINT NOT NULL,
                                         PRIMARY KEY (id),
                                         UNIQUE KEY uq_favorites_user_video (user_id, video_id),
                                         INDEX idx_favorites_user  (user_id),
                                         INDEX idx_favorites_video (video_id),
                                         CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(id)
                                             ON DELETE CASCADE,
                                         CONSTRAINT fk_favorites_video FOREIGN KEY (video_id) REFERENCES videos(id)
                                             ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) 리뷰 테이블
CREATE TABLE IF NOT EXISTS reviews (
                                       id         BIGINT NOT NULL AUTO_INCREMENT,
                                       user_id    BIGINT NOT NULL,
                                       video_id   BIGINT NOT NULL,
                                       title      VARCHAR(100) NOT NULL,
                                       content    TEXT NOT NULL,
                                       rating     INT NOT NULL,
                                       views      INT UNSIGNED DEFAULT 0,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       PRIMARY KEY (id),
                                       INDEX idx_reviews_user (user_id),
                                       INDEX idx_reviews_video (video_id),
                                       CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id)
                                           ON DELETE CASCADE,
                                       CONSTRAINT fk_reviews_video FOREIGN KEY (video_id) REFERENCES videos(id)
                                           ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) 댓글 테이블 (CASCADE 적용)
CREATE TABLE IF NOT EXISTS comments (
                                        id         BIGINT NOT NULL AUTO_INCREMENT,
                                        user_id    BIGINT NOT NULL,
                                        review_id  BIGINT NOT NULL,
                                        content    TEXT NOT NULL,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (id),
                                        INDEX idx_comments_user (user_id),
                                        INDEX idx_comments_review (review_id),
                                        CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id)
                                            ON DELETE CASCADE,
                                        CONSTRAINT fk_comments_review FOREIGN KEY (review_id) REFERENCES reviews(id)
                                            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6) 사용자 누적 이용 시간 추적 테이블
CREATE TABLE IF NOT EXISTS usage_time_trackers (
                                                  id         BIGINT NOT NULL AUTO_INCREMENT,
                                                  user_id    BIGINT NOT NULL,
                                                  total_time BIGINT NOT NULL DEFAULT 0,
                                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                  modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                  PRIMARY KEY (id),
                                                  UNIQUE KEY uq_usage_time_user (user_id),
                                                  CONSTRAINT fk_usage_time_user FOREIGN KEY (user_id) REFERENCES users(id)
                                                      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
