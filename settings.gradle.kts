rootProject.name = "is-tech-labs"

include("kotiki-app")
include("kotiki-app:controller")
findProject(":kotiki-app:controller")?.name = "controller"
include("kotiki-app:dao")
findProject(":kotiki-app:dao")?.name = "dao"
include("kotiki-app:service")
findProject(":kotiki-app:service")?.name = "service"
include("kotiki-app:service:models")
findProject(":kotiki-app:service:models")?.name = "models"