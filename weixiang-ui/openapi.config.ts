const { generateService } = require("@umijs/openapi");

generateService({
  requestLibPath: "import request from '@/request'",
  schemaPath: "http://localhost:8015/api/v3/api-docs",
  serversPath: "./src",
});
