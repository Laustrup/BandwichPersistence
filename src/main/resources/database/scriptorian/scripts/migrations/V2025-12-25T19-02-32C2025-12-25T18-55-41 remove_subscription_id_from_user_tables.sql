alter table organisation_employees
    drop constraint FK_organisation_employees__subscriptions,
    drop column subscription_id;

alter table artists
    drop constraint FK_artists__subscriptions,
    drop column subscription_id;