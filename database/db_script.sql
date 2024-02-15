DROP SCHEMA IF EXISTS blogs CASCADE;

CREATE SCHEMA IF NOT EXISTS blogs;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS blogs.users
(
    user_id           UUID PRIMARY KEY             DEFAULT uuid_generate_v4(),
    username          VARCHAR(50) UNIQUE  NOT NULL,
    email             VARCHAR(100) UNIQUE NOT NULL,
    first_name        VARCHAR(50)         NOT NULL,
    last_name         VARCHAR(50)         NOT NULL,
    profile_image_url TEXT                         DEFAULT NULL,
    hashed_password   VARCHAR(255)        NOT NULL,
    email_verified    BOOLEAN             NOT NULL DEFAULT FALSE,
    is_blocked        BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS blogs.roles
(
    role_id   SERIAL PRIMARY KEY,
    role_code VARCHAR(10) UNIQUE NOT NULL,
    role_name VARCHAR(50) DEFAULT NULL,
    role_desc TEXT        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.permissions
(
    permission_id   SERIAL PRIMARY KEY,
    permission_code VARCHAR(30) UNIQUE NOT NULL,
    permission_name VARCHAR(50) DEFAULT NULL,
    permission_desc TEXT        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.user_role
(
    user_id UUID    NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES blogs.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES blogs.roles (role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.role_permission
(
    role_id       INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES blogs.roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES blogs.permissions (permission_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.tags
(
    tag_id    SERIAL PRIMARY KEY,
    tag_name  VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN            NOT NULL DEFAULT TRUE,
    tag_desc  TEXT                        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.series
(
    series_id   SERIAL PRIMARY KEY,
    series_name VARCHAR(50) UNIQUE NOT NULL,
    is_active   BOOLEAN            NOT NULL DEFAULT TRUE,
    series_desc TEXT                        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.categories
(
    category_id        SERIAL PRIMARY KEY,
    category_name      VARCHAR(50) NOT NULL,
    is_active          BOOLEAN     NOT NULL DEFAULT TRUE,
    category_desc      TEXT                 DEFAULT NULL,
    parent_category_id INTEGER              DEFAULT NULL,
    FOREIGN KEY (parent_category_id) REFERENCES blogs.categories (category_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.authors
(
    author_id        SERIAL PRIMARY KEY,
    author_pseudonym VARCHAR(50) NOT NULL,
    user_id          UUID        NOT NULL,
    author_desc      TEXT DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES blogs.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.posts
(
    post_id             SERIAL PRIMARY KEY,
    author_id           INTEGER      NOT NULL,
    series_id           INTEGER                  DEFAULT NULL,
    category_id         INTEGER                  DEFAULT NULL,
    post_title          VARCHAR(255) NOT NULL,
    thumbnail_image_url TEXT                     DEFAULT NULL,
    post_status         VARCHAR(10)  NOT NULL,
    published_at        TIMESTAMP WITH TIME ZONE DEFAULT NULL,
    slug                TEXT         NOT NULL,
    FOREIGN KEY (author_id) REFERENCES blogs.authors (author_id) ON DELETE CASCADE,
    FOREIGN KEY (series_id) REFERENCES blogs.series (series_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES blogs.categories (category_id) ON DELETE CASCADE,
    CONSTRAINT valid_status CHECK (post_status IN ('DRAFT', 'PENDING', 'PUBLISHED', 'ARCHIVED'))
);

CREATE TABLE IF NOT EXISTS blogs.post_tag
(
    post_id INTEGER NOT NULL,
    tag_id  INTEGER NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES blogs.posts (post_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES blogs.tags (tag_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.post_versions
(
    post_versions_id SERIAL PRIMARY KEY,
    post_id          INTEGER NOT NULL,
    content          TEXT    NOT NULL,
    version_number   INTEGER NOT NULL,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT NULL,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
    FOREIGN KEY (post_id) REFERENCES blogs.posts (post_id) ON DELETE CASCADE,
    UNIQUE (post_id, version_number)
);



INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('Python',
        'Python is a programming language that lets you work quickly and integrate systems more effectively.');
INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('Java', 'Java is a programming language and computing platform first released by Sun Microsystems in 1995.');
INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('JavaScript', 'JavaScript is a programming language that conforms to the ECMAScript specification.');
INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('C++',
        'C++ is a general-purpose programming language created by Bjarne Stroustrup as an extension of the C programming language, or "C with Classes".');
INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('C#',
        'C# is a general-purpose, multi-paradigm programming language encompassing strong typing, lexically scoped, imperative, declarative, functional, generic, object-oriented, and component-oriented programming disciplines.');
INSERT INTO blogs.tags (tag_name, tag_desc)
VALUES ('PHP', 'PHP is a general-purpose scripting language especially suited to web development.');


INSERT INTO blogs.categories (category_name, category_desc, parent_category_id)
VALUES ('Programming',
        'Programming is the process of creating a set of instructions that tell a computer how to perform a task.',
        NULL);
INSERT INTO blogs.categories (category_name, category_desc, parent_category_id)
VALUES ('Web Development',
        'Web development is the work involved in developing a Web site for the Internet or an intranet.', 1);
INSERT INTO blogs.categories (category_name, category_desc, parent_category_id)
VALUES ('Mobile Development',
        'Mobile app development is the act or process by which a mobile app is developed for mobile devices, such as personal digital assistants, enterprise digital assistants or mobile phones.',
        2);
INSERT INTO blogs.categories (category_name, category_desc, parent_category_id)
VALUES ('Desktop Development',
        'Desktop development is the act or process by which a desktop app is developed for desktop devices, such as personal computers, laptops, etc.',
        1);
INSERT INTO blogs.categories (category_name, category_desc, parent_category_id)
VALUES ('Game Development', 'Game development is the process of creating a video game.', 1);


INSERT INTO blogs.series (series_name, series_desc)
VALUES ('Python',
        'Python is a programming language that lets you work quickly and integrate systems more effectively.');
INSERT INTO blogs.series (series_name, series_desc)
VALUES ('Java', 'Java is a programming language and computing platform first released by Sun Microsystems in 1995.');
INSERT INTO blogs.series (series_name, series_desc)
VALUES ('JavaScript', 'JavaScript is a programming language that conforms to the ECMAScript specification.');

-- INSERT INTO blogs.authors (author_pseudonym, user_id, author_desc)
-- VALUES ('John Doe', '84484015-9e15-440a-9ee2-54ff3ca3899f', 'John Doe is a software engineer.');
--
--
-- INSERT INTO blogs.posts (author_id, series_id, category_id, post_title, thumbnail_image_url, post_status, published_at,
--                          slug)
-- VALUES (1, 1, 1, 'Python Tutorial', 'https://i.imgur.com/7bMqy.jpg', 'PUBLISHED', '2020-01-01 00:00:00',
--         'python-tutorial');
--
-- INSERT INTO blogs.post_tag (post_id, tag_id)
-- VALUES (2, 1);