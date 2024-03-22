rootProject.name = "is-tech-labs"
include("lab1-banks")
include("lab1-application")
include("lab1-banks:lab1-application")
findProject(":lab1-banks:lab1-application")?.name = "lab1-application"
include("lab1-banks:lab1-data")
findProject(":lab1-banks:lab1-data")?.name = "lab1-data"
include("lab1-banks:lab1-presentation")
findProject(":lab1-banks:lab1-presentation")?.name = "lab1-presentation"
include("lab1-banks:lab1-application:contracts")
findProject(":lab1-banks:lab1-application:contracts")?.name = "contracts"
include("lab1-banks:lab1-application:infrastructure")
findProject(":lab1-banks:lab1-application:infrastructure")?.name = "infrastructure"
include("lab1-banks:lab1-application:models")
findProject(":lab1-banks:lab1-application:models")?.name = "models"
