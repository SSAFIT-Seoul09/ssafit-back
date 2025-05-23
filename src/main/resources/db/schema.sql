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
                                      views       INT UNSIGNED DEFAULT 0,
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

# -- 4) 영상 조회 로그 테이블
# CREATE TABLE IF NOT EXISTS video_viewlogs (
#     id        BIGINT NOT NULL AUTO_INCREMENT,
#     user_id   BIGINT NOT NULL,
#     video_id  BIGINT NOT NULL,
#     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
#     PRIMARY KEY (id),
#     UNIQUE KEY uq_vv_user_video (user_id, video_id),
#     INDEX idx_vv_user  (user_id),
#     INDEX idx_vv_video (video_id),
#     CONSTRAINT fk_vv_user
#         FOREIGN KEY (user_id)
#         REFERENCES users(id),
#     CONSTRAINT fk_vv_video
#         FOREIGN KEY (video_id)
#         REFERENCES videos(id)
#     ) ENGINE=InnoDB
#     DEFAULT CHARSET=utf8mb4;

-- 5) 리뷰 테이블
CREATE TABLE IF NOT EXISTS reviews (
                                       id       BIGINT NOT NULL AUTO_INCREMENT,
                                       user_id  BIGINT NOT NULL,
                                       video_id BIGINT NOT NULL,
                                       title    VARCHAR(100) NOT NULL,
                                       content  TEXT    NOT NULL,
                                       rating   INT     NOT NULL,
                                       views    INT UNSIGNED DEFAULT 0,
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

# -- 6) 리뷰 조회 로그 테이블
# CREATE TABLE IF NOT EXISTS reviews_viewlogs (
#     id        BIGINT NOT NULL AUTO_INCREMENT,
#     user_id   BIGINT NOT NULL,
#     review_id BIGINT NOT NULL,
#     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
#     PRIMARY KEY (id),
#     UNIQUE KEY uq_rv_user_review (user_id, review_id),
#     INDEX idx_rv_user   (user_id),
#     INDEX idx_rv_review (review_id),
#     CONSTRAINT fk_rv_user
#         FOREIGN KEY (user_id)
#         REFERENCES users(id),
#     CONSTRAINT fk_rv_review
#         FOREIGN KEY (review_id)
#         REFERENCES reviews(id)
#     ) ENGINE=InnoDB
#     DEFAULT CHARSET=utf8mb4;

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
INSERT INTO videos (
    id, user_id, title, description, part, url, views, created_at, modified_at
) VALUES
      (1, 1, '초보자용 ARM 스트레칭', '하루 5분으로 끝내는 팔 스트레칭 루틴', 'ARM', 'https://cdn.example.com/videos/arm_stretch_beginner.mp4', 120, NOW(), NOW()),
      (2, 1, '고급 LEG 강화 운동',     '하체 근력 최적화를 위한 심화 루틴',       'LEG',       'https://cdn.example.com/videos/leg_strength_advanced.mp4',    345, NOW(), NOW()),
      (3, 1, '전신 FULL_BODY 워밍업',    '운동 전 모든 부위를 풀어주는 전신 워밍업',   'FULL_BODY', 'https://cdn.example.com/videos/fullbody_warmup.mp4',          210, NOW(), NOW()),
      (4, 2, 'BACK 집중 코어 운동',    '허리 및 등 근육 강화에 좋은 코어 운동',     'BACK',      'https://cdn.example.com/videos/back_core_workout.mp4',        89,  NOW(), NOW()),
      (5, 2, 'CHEST 펌핑 루틴',       '가슴 근육 발달을 위한 4가지 동작',         'CHEST',     'https://cdn.example.com/videos/chest_pumping.mp4',             157, NOW(), NOW()),
      (6, 2, 'LEG 스트레칭 & 쿨다운', '하체 피로 회복을 돕는 스트레칭',          'LEG',       'https://cdn.example.com/videos/leg_cooldown.mp4',              64,  NOW(), NOW()),
      (7, 3, 'FULL_BODY HIIT 세션',   '전신 지방 연소를 위한 HIIT 루틴',           'FULL_BODY', 'https://cdn.example.com/videos/fullbody_hiit.mp4',             512, NOW(), NOW()),
      (8, 3, 'ARM 이두근 집중 운동',   '이두근 볼륨업을 위한 슈퍼세트',            'ARM',       'https://cdn.example.com/videos/arm_biceps_superset.mp4',       230, NOW(), NOW()),
      (9, 3, 'CHEST & BACK 콤보',    '가슴과 등 균형 강화 운동',                'CHEST',     'https://cdn.example.com/videos/chest_back_combo.mp4',          298, NOW(), NOW());



-- 리뷰
INSERT INTO reviews (id, user_id, video_id, title, content, rating, views, created_at, modified_at)
VALUES (1, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, 0, now(), now());

INSERT INTO reviews (id, user_id, video_id, title, content, rating, views, created_at, modified_at)
VALUES (2, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, 0, now(), now());

INSERT INTO reviews (id, user_id, video_id, title, content, rating, views, created_at, modified_at)
VALUES (3, 1, 1, '테스트', '리뷰작성 주저리주저리', 5, 0, now(), now());


