# Elo Management Discord Bot

This bot tracks and manages users' Elo ratings, providing commands to manipulate and view rankings easily.

## Features
- Stores and manages user Elo ratings.
- Offers a suite of commands for queue management and match handling.

## Commands

### Queue Management

1. **Send Queue Message**
    - **Description:** Sends the queue message.
    - **Command:** `=sendqueue`

2. **Lock Queue**
    - **Description:** Locks the queue to prevent additional users from joining.
    - **Command:** `=lock`

3. **Unlock Queue**
    - **Description:** Unlocks the queue to allow users to join.
    - **Command:** `=unlock`

### Match Management

4. **Close Game**
    - **Description:** Closes the current game session.
    - **Command:** `=close`
  
5. **Force Win**
    - **Description:** Forcefully assigns a win to a specified team.
    - **Command:** `=forcewin <team_number>`

### Elo Management

6. **Remove Elo**
    - **Description:** Removes a specified amount of Elo from a user.
    - **Command:** `=removeelo <user> <amount>`

7. **Add Elo**
    - **Description:** Adds a specified amount of Elo to a user.
    - **Command:** `=addelo <user> <amount>`

### Usage Examples

- **Send Queue Message:** `=sendqueue`
- **Lock Queue:** `=lock`
- **Unlock Queue:** `=unlock`
- **Close Game:** `=close`
- **Force Win for Team 1:** `=forcewin 1`
- **Remove 50 Elo from User123:** `=removeelo User123 50`
- **Add 30 Elo to User123:** `=addelo User123 30`

### Last updated:
-  July 22, 2021
