# Genovia Consultation API

A small Spring Boot service that exposes a consultation questionnaire and evaluates basic eligibility rules to be prescribed the Genovian Pear antidote.


## Running the service
Prerequesites:
- Java21
- Maven (mvnw wrapper is in the repo)

running the service should be possible in 3 ways:
```
#from the root of the project
    mvn spring-boot:run
or
    ./mvnw spring-boot:run
```
or simply opening the project in intellij and running from GenoviaApplication.java

## Package Structure

I kept things fairly simple, the resource package contains the endpoints and the service package contains all the business logic, everything else is just neat wrappers for DTOs or ExceptionMappers

## Assumptions

- No security!
- No data persistence!
- No docker etc, we're happy to just run JAR files etc for now


## Endpoints

### `GET /consultation/questions`
Returns the list of consultation questions, including type, required flag, options, and any attached eligibility rules.

### `POST /consultation/evaluate`
Accepts a list of answers and returns a preliminary eligibility decision.

**Example request:**

## Testing the Endpoints

I just used curl for this, the GET endpoint is simple enough to test

```curl http://localhost:8080/consultation/questions```

We can also test happy and sad paths for the POST endpoint with the following curl examples, using the question IDs that we got from the GET endpoint.

```
curl -X POST http://localhost:8080/consultation/evaluate   -H "Content-Type: application/json"   -d '{
    "answers": [
      { "questionId": "age", "answer": "2" },
      { "questionId": "adverse_reaction", "answer": "false" },
      { "questionId": "current_symptoms", "answer": "true" },
      { "questionId": "pear_intake_frequency", "answer": "Once a week" },
      { "questionId": "other_conditions", "answer": "None" },
      { "questionId": "preferred_pear_form", "answer": "Fresh pears" }
    ]
  }'

# expect too young response

curl -X POST http://localhost:8080/consultation/evaluate   -H "Content-Type: application/json"   -d '{
    "answers": [
      { "questionId": "age", "answer": "25" },
      { "questionId": "adverse_reaction", "answer": "false" },
      { "questionId": "current_symptoms", "answer": "true" },
      { "questionId": "pear_intake_frequency", "answer": "Once a week" },
      { "questionId": "other_conditions", "answer": "None" },
      { "questionId": "preferred_pear_form", "answer": "Fresh pears" }
    ]
  }'
  
# expect preliminary success!

curl -X POST http://localhost:8080/consultation/evaluate   -H "Content-Type: application/json"   -d '{
    "answers": [
      { "questionId": "age", "answer": "jeff" },
      { "questionId": "adverse_reaction", "answer": "false" },
      { "questionId": "current_symptoms", "answer": "true" },
      { "questionId": "pear_intake_frequency", "answer": "Once a week" },
      { "questionId": "other_conditions", "answer": "None" },
      { "questionId": "preferred_pear_form", "answer": "Fresh pears" }
    ]
  }'
  
# expect data type error response

curl -X POST http://localhost:8080/consultation/evaluate   -H "Content-Type: application/json"   -d '{
    "answers": [
      { "questionId": "age", "answer": "25" },
      { "questionId": "adverse_reaction", "answer": "false" }
    ]
  }'
  
# expect missing questions response

etc
```

## Business Logic (and Extensions)

The main logic I got down was the foundations of a generic rule system for our questions - the main goal of this is to be able to dynamically create new rules without needing to add any code. Ideally a future deploy would allow for consultation forms to be created entirely externally to the code base.

The business logic here is just a simple MVP of that concept, we can add a lot of depth to include multiple choice questions (or combinations of certain multiple choice answers etc etc) but for now I just stuck with a couple of simple boolean and numerical operations, but hopefully this proves the concept that the questions can be closed logic units which contain both the question and any disqualifying criteria in the same object (also making sure to @JsonIgnore the actual disqualifying criteria from the endpoint response since it's only used internally).

Extending the questions endpoint to cover different conditions should also be simple - we can add a simple URL parameter (no request body for GET endpoints), which can specify the condition and therefore the sublist of questions that we need.

## What I Would Have Added With More Time

- Unit tests (see below)
- Proper validation
  - validation is still done inside the ConsultationService - I really don't like this, with any more time the first priority would be cleaning up that validation, adding a validator class and @Valid annotation on the endpoint/pojo.
- Data storage for questions
  - I'd probably choose something simple, I think this is a fairly good use case for noSQL, as we don't ever need to make complex queries, and the flexible structure gives us future-proofing without needing to run any migrations.
- Better typing
  - `String type` in the questions is definitely not my favourite feature, at the very least I'd like this to be extended to an enum, but objectmapper could deal with this fairly cleanly with a custom mapper to subclasses perhaps?
- (Probably would also have chosen to prioritise any of these higher than exception mapping in hindsight)

## Unit Tests

- In a production situation, I would absolutely unit test this service, however, given the fairly tight time constraints, I chose not to write unit tests for this exercise, I figured they would eat into the time allowance pretty heavily and don't offer too much value to you guys when trying to interview - hopefully that's ok!
- When testing, I'd stick with what I know, JUnit5 and Mockito