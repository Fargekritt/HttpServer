# Amund's Http server

This project is my attempt to make an HTTP server with sockets with as few 3rd party libraries as possible.

# TODO:
 * [ ] HttpRequest
   * [ ] HttpRequestParser
     * [ ] Parse status line
       * [x] Split the status line up into the elements
       * [ ] Validate data
         * [ ] Validate methode
         * [ ] Validate uri
         * [ ] Validate Version
     * [x] Parse Headers
       * [x] split HeaderName and headerValue
       * [x] Split headerValue into a list
       * [x] Validate data
     * [ ] Parse Body
       * [ ] Based on Content-Length 
       * [ ] Validate data
 * [ ] Logging
 * [ ] HttpResponse
   * [ ] HttpResponseBuilder



* [ ] Testing

