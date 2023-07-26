-- cache_handler.lua

local cache = ngx.shared.cache
local cjson = require("cjson")

local function is_image_request()
    local uri = ngx.var.uri
    return string.match(uri, "%.png$") or string.match(uri, "%.jpg$") or string.match(uri, "%.jpeg$")
end

local function increment_request_count(uri)
    local req_count = cache:get(uri) or 0
    req_count = req_count + 1
    cache:set(uri, req_count)
end

local function handle_cache()
    local uri = ngx.var.uri

    if is_image_request() then
        local req_count = cache:get(uri) or 0

        if req_count >= 1 then
            ngx.header["X-CACHE"] = "HIT"
            increment_request_count(uri)
        else
            ngx.header["X-CACHE"] = "MISS"
            increment_request_count(uri)
        end
    end
end

local function purge_cache()
    ngx.req.read_body()
    local data = ngx.req.get_body_data()
    if data then
        local uri_to_purge = cjson.decode(data)
        if uri_to_purge then
            cache:delete(uri_to_purge)
            ngx.status = 204
            ngx.exit(ngx.HTTP_NO_CONTENT)
        end
    end
    ngx.exit(ngx.HTTP_BAD_REQUEST)
end

local _M = {
    handle_cache = handle_cache,
    purge_cache = purge_cache
}

return _M
