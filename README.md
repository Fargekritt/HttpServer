[![Tests](https://github.com/Fargekritt/HttpServer/actions/workflows/workflow.yml/badge.svg)](https://github.com/Fargekritt/HttpServer/actions/workflows/workflow.yml)
# Amund's Http server

This project is my attempt to make an HTTP server with sockets with as few 3rd party libraries as possible.

# TODO:
 * [ ] HttpRequest
   * [ ] Cookies
     * [ ] Needs to be properly parsed
     * How even work?
   * [x] HttpRequestParser
     * [x] Parse status line
       * [x] Split the status line up into the elements
       * [X] Validate data
         * [x] Validate method
         * [ ] Validate uri
         * [ ] Validate Version
     * [x] Parse Headers
       * [x] split HeaderName and headerValue
       * [X] Split headerValue into a list (Need to look into this, some headerField should and some not)
       * [x] Validate data
     * [x] Parse Body
       * [x] Based on Content-Length 
       * [ ] Validate data
 * [ ] Logging
 * [ ] HttpResponse
   * [ ] HttpResponseBuilder
 * [ ] Sockets
   * [ ] Client Socket
   * [ ] Server Socket

* [ ] Testing
  * [ ] HttpRequestParser
    * [x] Basic test with simple input
    * [x] Basic test with simple invalid input
    * [ ] Basic test with complex input 
    * [ ] Basic test with complex invalid input
    * [x] Valid methods (GET, POST DELETE, PUT)
    * [ ] Valid HTTP version
  * [ ] HttpResponseBuilder
    * [ ] Basic test with simple output
    * [ ] Basic test with invalid output