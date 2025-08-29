alter table events
    drop constraint FK_events__chat_rooms,
    drop column chat_room_id;

