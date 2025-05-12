return {
  "nvimtools/none-ls.nvim",
  config = function()
    local null_ls = require("null-ls")
    null_ls.setup({
      sources = {
        -- Lua
        null_ls.builtins.formatting.stylua,
        -- JavaScript, TypeScript, HTML, CSS, JSON, YAML
        null_ls.builtins.formatting.prettier.with({
          filetypes = { "javascript", "typescript", "html", "css", "json", "yaml",},
        }),
        -- Rust
        null_ls.builtins.formatting.rustfmt,
        --null_ls.builtins.formatting.black,
        -- Python
        null_ls.builtins.formatting.black.with({
          command = "/home/batman/.local/share/nvim/mason/bin/black"
        }),
        -- Go
        null_ls.builtins.formatting.gofmt,
        null_ls.builtins.formatting.goimports,
      },
    })

    -- Keymap for formatting
    vim.keymap.set("n", "<C-o>", function()
      vim.lsp.buf.format({ async = true })
    end, {})
  end,
}
