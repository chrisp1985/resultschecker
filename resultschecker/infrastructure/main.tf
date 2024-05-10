provider "aws" {
  region = "eu-west-2"
  access_key = var.access_key_value
  secret_key = var.secret_key_value
}

module "lambda" {
  source = "./modules/lambda"
}

module "eventbridge" {
  source = "./modules/eventbridge"
  depends_on = [module.lambda]
}

resource "aws_scheduler_schedule" "football_score_updater_tf" {
  name       = "football_score_updater_tf"
  description = "An eventbridge scheduler to trigger the football score updater lambda (provisioned through Terraform)."
  group_name = "default"

  flexible_time_window {
    maximum_window_in_minutes = 30
    mode = "FLEXIBLE"
  }

  schedule_expression = "cron(0 */5 * * ? *)"

  target {
    role_arn = module.eventbridge.eventbridge_role_arn
    arn      = module.lambda.lambda_arn
  }
}