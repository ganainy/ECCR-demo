# Cyber Security Technical Demo

This project was developed as part of the technical demo for the master's module "Emerging Challenges in Cyber Security."

## Project Overview

The repository contains two versions of an application:
- **Non-compliant App** (Main branch)
- **GDPR-compliant App** (gdpr-compliant branch)

Both applications have the same appearance but differ in their handling of user privacy.

### Non-compliant App
- **Branch**: `main`
- **Description**: This version employs methods that sidestep proper channels to access protected user information. The purpose is to facilitate targeted and local advertising without proper user consent.

### GDPR-compliant App
- **Branch**: `gdpr-compliant`
- **Description**: This version adheres to GDPR guidelines, ensuring that user privacy is respected and proper consent is obtained before accessing any protected user information.

## API Backend

Both applications connect to a custom API built using Flask and Python, hosted on Heroku. Backend files can be found under the `ECCR-demo/flask_api` directory.

## Man-in-the-Middle Proxy

To analyze the TLS traffic between applications and servers, Fiddler Everywhere was utilized. This tool provided insight into the GET and POST requests,
enabling a comprehensive understanding of their behavior. Additionally, it helped identify which applications were compliant with security protocols and which were not.

## Non-compliant App Vidoes

https://github.com/ganainy/ECCR-demo/assets/44480226/a4fc8bd1-4bcf-4104-9a5c-28b48b602b15


https://github.com/ganainy/ECCR-demo/assets/44480226/a092841f-7c78-4a1b-9fce-ceed70889390


https://github.com/ganainy/ECCR-demo/assets/44480226/bcc616d3-b86f-453b-b66c-0bae61bedab7


## Compliant App Vidoes

https://github.com/ganainy/ECCR-demo/assets/44480226/309e575c-1145-451f-b04a-a381e0157623


https://github.com/ganainy/ECCR-demo/assets/44480226/321bee28-8793-4a08-b3bd-f793f3350633


https://github.com/ganainy/ECCR-demo/assets/44480226/e99e3057-a32c-4ddd-9c6a-6c2972542bce

https://github.com/ganainy/ECCR-demo/assets/44480226/d1ef4aea-b19d-4cb4-859c-fb2734347483
