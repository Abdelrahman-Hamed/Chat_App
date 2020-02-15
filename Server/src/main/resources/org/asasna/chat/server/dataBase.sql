-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema chat_app
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema chat_app
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chat_app` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `chat_app` ;

-- -----------------------------------------------------
-- Table `chat_app`.`statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_app`.`statuses` (
  `id` INT NOT NULL,
  `status_value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chat_app`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_app`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `phone_number` INT NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `picture_path` VARCHAR(100) NULL DEFAULT NULL,
  `password` VARCHAR(45) NOT NULL,
  `gender` INT NOT NULL DEFAULT '0',
  `country` VARCHAR(30) NOT NULL,
  `bio` VARCHAR(100) NULL DEFAULT NULL,
  `date_of_birth` DATE NULL DEFAULT NULL,
  `verified` TINYINT(1) NULL DEFAULT '0',
  `chatbotoption` TINYINT(1) NULL DEFAULT '0',
  `status_id` INT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fk_status_user_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_status_user`
    FOREIGN KEY (`status_id`)
    REFERENCES `chat_app`.`statuses` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chat_app`.`contacts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_app`.`contacts` (
  `first_member` INT NOT NULL,
  `second_member` INT NOT NULL,
  PRIMARY KEY (`first_member`, `second_member`),
  INDEX `second_member` (`second_member` ASC) VISIBLE,
  CONSTRAINT `contacts_ibfk_1`
    FOREIGN KEY (`first_member`)
    REFERENCES `chat_app`.`users` (`id`),
  CONSTRAINT `contacts_ibfk_2`
    FOREIGN KEY (`second_member`)
    REFERENCES `chat_app`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chat_app`.`invitations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_app`.`invitations` (
  `from_id` INT NOT NULL,
  `to_id` INT NOT NULL,
  PRIMARY KEY (`from_id`, `to_id`),
  INDEX `to_id` (`to_id` ASC) VISIBLE,
  CONSTRAINT `invitations_ibfk_1`
    FOREIGN KEY (`from_id`)
    REFERENCES `chat_app`.`users` (`id`),
  CONSTRAINT `invitations_ibfk_2`
    FOREIGN KEY (`to_id`)
    REFERENCES `chat_app`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
