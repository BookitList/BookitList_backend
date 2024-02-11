INSERT INTO book (title, author, publisher, pub_date, description, link, isbn13, price, cover, like_count, deleted)
VALUES ('Aladdin and His Lamp (반양장) - and the Other Stories', 'Harriette Taylor Treadwel, Margaret Free (지은이)',
        '레드버드(Redbud)', '2009-12-01',
        'Reading Classics with Redbud 시리즈 8권. 어린이들에게 영어로 된 세계명작 동화들을 읽게 함으로써 영어 문장을 읽고 이해하는 능력을 갖도록 도와주는 시리즈로 어린이들에게 알맞은 동요나 동시를 포함하고 있다.',
        'http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=6330826&amp;partner=openAPI&amp;start=api', '9788931514810',
        7200, 'https://image.aladin.co.kr/product/633/8/cover500sum/8931514816_1.jpg', 0, false),
       ('Aladdin and His Lamp (양장) - and the Other Stories', 'Harriette Taylor Treadwel, Margaret Free (지은이)',
        '레드버드(Redbud)', '2009-12-01',
        'Reading Classics with Redbud 시리즈 8권. 어린이들에게 영어로 된 세계명작 동화들을 읽게 함으로써 영어 문장을 읽고 이해하는 능력을 갖도록 도와주는 시리즈로 어린이들에게 알맞은 동요나 동시를 포함하고 있다.',
        'http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=6330794&amp;partner=openAPI&amp;start=api', '9788931514803',
        8100, 'https://image.aladin.co.kr/product/633/7/cover500sum/8931514808_1.jpg', 0, false),
       ('[POD] The Story of Aladdin; or, the Wonderful Lamp (영문판) - 알라딘과 마술램프', '작가미상 (지은이)', '부크크(bookk)',
        '2019-07-18', NULL,
        'http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=202032448&amp;partner=openAPI&amp;start=api',
        '9791127278199', 12200, 'https://image.aladin.co.kr/product/20203/24/cover500sum/k252635643_1.jpg', 0, false),
       ('디즈니 알라딘 - 파 프롬 아그라바', '아이샤 사이드 (지은이), 김미선 (옮긴이)', '아르누보', '2019-09-25',
        '2019년 최고의 화제작 영화 〈알라딘〉의 세계관을 관통하는 디즈니 오리지널 소설. 아그라바를 떠나 시공간을 넘나드는 마법이 공존하는 세계에서 신비하고 긴장감 넘치는 모험담이 펼쳐진다.',
        'http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=209468603&amp;partner=openAPI&amp;start=api',
        '9791187824824', 14400, 'https://image.aladin.co.kr/product/20946/86/cover500sum/k822636271_1.jpg', 0, false);

INSERT INTO member (email, name, oauth2Id, auth_provider, follow_count, deleted, created_at, modified_at)
VALUES ('test@gmail.com', 'test', 'test', 'KAKAO', 0, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('test2@gmail.com', 'test2', 'test2', 'KAKAO', 0, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO post (member_id, book_id, title, content, status, like_count, view_count, deleted)
VALUES (1, 1, 'postTitle', 'postContent', 'PUBLIC', 0, 0, false),
       (2, 1, 'postTitle1', 'Content', 'PUBLIC', 2, 0, false),
       (2, 1, 'postTitle2', '제목', 'PUBLIC', 0, 0, false),
       (2, 1, 'postTitle3', 'post', 'PUBLIC', 0, 0, false),
       (2, 1, 'privateTitle', 'privateContent', 'PRIVATE', 0, 0, false),
       (2, 2, 'posTitle', 'ptent', 'PUBLIC', 0, 0, false),
       (2, 2, 'positle', 'postent', 'PUBLIC', 0, 0, false),
       (2, 2, 'privateTitle', 'privateContent', 'PRIVATE', 0, 0, false);

INSERT INTO review (member_id, book_id, content, like_count, view_count, deleted)
VALUES (1, 1, 'reviewContent', 0, 0, false),
       (2, 1, 'reviewContent1', 2, 0, false),
       (2, 1, 'rContent', 0, 0, false),
       (2, 1, 'rContent2', 0, 0, false),
       (2, 2, 'rContent3', 0, 0, false),
       (2, 2, 'reContent2', 0, 0, false);

INSERT INTO post_like (member_id, post_id)
VALUES (1, 2),
       (2, 2);

INSERT INTO review_like (member_id, review_id)
VALUES (1, 2),
       (2, 2);

INSERT INTO book_like (book_id, member_id)
VALUES (1, 1),
       (2, 1);
