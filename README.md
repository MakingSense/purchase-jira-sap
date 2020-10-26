# Overview

This repository contains a micro-service project that migrates information that Jira send about purchases, map them to SAP models and saves them.

## Flow

The flow is going to be triggered by Jira when a new purchase ticket is resolved. When that event happens it will create a new REST request with the mandatory information of the ticket so it can be migrated to SAP.

## Tech stack

The technologies used for this project are:
    * Java 8
    * Spring Framework
    * Spring Boot
    * Maven

