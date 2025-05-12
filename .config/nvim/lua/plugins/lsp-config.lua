return {
  {
    "williamboman/mason.nvim",
    lazy = false,
    config = function()
      require("mason").setup()
    end,
  },
  {
    "williamboman/mason-lspconfig.nvim",
    lazy = false,
    opts = {
      ensure_installed = { "lua_ls", "tsserver", "html", "cssls", "gopls", "pyright", "clangd", "tailwindcss", "rust_analyzer" },
    },
  },
  {
    "neovim/nvim-lspconfig",
    lazy = false,
    config = function()
      local capabilities = require('cmp_nvim_lsp').default_capabilities()

      local lspconfig = require("lspconfig")
      lspconfig.tsserver.setup({
        capabilities = capabilities
      })
      lspconfig.html.setup({
        capabilities = capabilities
      })
      lspconfig.lua_ls.setup({
        capabilities = capabilities
      })
      lspconfig.gopls.setup({ -- added configuration for gopls
        capabilities = capabilities
      })
      lspconfig.pyright.setup({
        capabilities = capabilities
      })
      lspconfig.cssls.setup({
        capabilities = capabilities
      })
      lspconfig.clangd.setup({
        capabilities = capabilities
      })
      lspconfig.tailwindcss.setup({
        capabilities = capabilities
      })
      lspconfig.rust_analyzer.setup({
        capabilities = capabilities,
        settings = {
          ["rust-analyzer"] = {
            diagnostics = {
              enable = true,             -- Enable diagnostics
              enableExperimental = true, -- Enable experimental diagnostics
            },
            checkOnSave = {
              enable = true,      -- Enable checking on save
              command = "clippy", -- Optional: you can use "check" or "clippy" for different linting
            },
          },
        },
      })
      vim.keymap.set("n", "K", vim.lsp.buf.hover, {})
      vim.keymap.set("n", "<C-z>", vim.lsp.buf.definition, {})
      vim.keymap.set("n", "<C-i>", vim.lsp.buf.references, {})
      vim.keymap.set("n", "<C-a>", vim.lsp.buf.code_action, {})
      vim.keymap.set("n", "<C-g>", vim.lsp.buf.rename, {})
    end,
  },
}
