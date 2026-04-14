def main():
    aws = open("./credentials", mode="r", encoding="utf-8")
    if aws.mode == "r":
        lines = aws.readlines()
        for line in lines:
            if line.startswith("region"):
                print(line.replace("region = ", "$env:AWS_REGION="), end='')
            if line.find("AWS_ACCESS_KEY_ID") > 0:
                lineParts = line.partition('AWS_ACCESS_KEY_ID=')
                print(f'$env:AWS_ACCESS_KEY_ID={lineParts[2]}', end='')
            if line.startswith("AWS_SECRET_ACCESS_KEY"):
                print(line.replace("AWS_SECRET_ACCESS_KEY=", "$env:AWS_SECRET_ACCESS_KEY="), end='')
            if line.startswith("AWS_SESSION_TOKEN"):
                print(line.replace("AWS_SESSION_TOKEN=", "$env:AWS_SESSION_TOKEN="), end='')

main()