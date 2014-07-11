# HConsole
HConsole is a web based simulation of a UNIX-style terminal. I implemented it just out of curiosity to see what could
be done, but along the way I imagined this could be further extended to actually provide remote console capabilities
or access to administrative features. This could also be used as an educational tool to show the basics of a UNIX
command line.

There are two part for this project. One is the backend: which is implemented in Java using Jersey for web RESTful
mappings and Jackson for JSON parsing and generation. The second part is a small html page with a CSS stylesheet
and a Javascript file which integrates a sample client.

You can visit the deployed example at: TBD

The documentation below shows the different endpoints exposed by the web app. You can also see this documentation
at: http://docs.hconsole.apiary.io/

# Console
Endpoints to handle a console session

## Create a new console session [/hconsole/session]

Before commands can be sent a session must be created. The service will return a session Id
which will be used in subsequent calls to interact with the service.

### Create a new console session [POST]

+ Response 200 (application/json)

        { "sessionId": "c9990410-84ef-470f-9279-20975cd8c925" }

## Send a session command [/hconsole/session/{sessionId}/command]

Once a session has been established, using this method you can send commands to the
service. Commands are equivalente to a one line entry on a UNIX terminal, for example ls -al
or cd mydir. The service will return a payload describing what needs to be drawn on the client screen.
There are two parts to the response: text to be appended to the tail of the client
screen and specific text posted to specific screen coordenates. Finally, there is
an optional parameter to direct the client screen to be cleared.

For example, an output like this:

    $ ls -al
    total 4
    drwxr-xr-x  3 anonymous admin 102 Jun  3 16:54 .
    drwxr-xr-x 24 anonymous admin 816 Jun  3 16:53 ..
    -rw-r--r--  1 anonymous admin   3 Jun  3 16:54 afile.txt

Would be presented on a format like the one on the example below.

### Send a session command [PUT]
+ Parameters
    + sessionId (required, string, `c9990410-84ef-470f-9279-20975cd8c925`) ... Session Id.

+ Request (application/json)

        { "command": "ls -al" }

+ Response 201 (application/json)

        {
            "response": "total 4\r\ndrwxr-xr-x  3 anonymous admin 102 Jun  3 16:54 .\r\ndrwxr-xr-x 24 anonymous admin 816 Jun  3 16:53 ..\r\n-rw-r--r--  1 anonymous admin   3 Jun  3 16:54 afile.txt",
            "clearScreen" : "false",
            "prompt": "$",
            "xyDraw" :
            [
                {
                    x:3,
                    y:4,
                    text:"Top"
                },
                {
                    x:3,
                    y:5,
                    text:"Bottom"
                }
            ]
        }

## End a session [/hconsole/{sessionId}]

### End a session [DELETE]
+ Response 200

        { "result": "success" }