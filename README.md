# spring-meet-together-api

## local setup

Run with Spring Profile 'local':

- set JVM Environment: -Dspring.profiles.active=local

## Deploy application

When already set up:
`git push heroku`

Otherwise, have to connect to heroku first, more information on their website.

### Heroku config

Set environment variables:

- SPRING_PROFILES_ACTIVE=prod
- MONGO_URI (URI to the mongodb including database-name, e.g. in the application-local.properties)
- JWT_SECRET (any long string)
- EMAIL_USER (gmail user)
- EMAIL_PASSWORD (gmail password)
