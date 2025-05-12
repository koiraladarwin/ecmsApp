return {
  "nvim-tree/nvim-tree.lua",
  dependencies = {
    "nvim-lua/plenary.nvim",
    "nvim-tree/nvim-web-devicons",
  },
  config = function()
    -- Set up NvimTree
    require("nvim-tree").setup({
      view = {
        width = 30,
        side = "left",
        auto_resize = true,
      },
      filters = {
        dotfiles = false,
      },
      git = {
        enable = true,
      },
    })
    vim.keymap.set("n", "<C-n>", ":NvimTreeToggle<CR>", { noremap = true, silent = true })
    vim.keymap.set("n", "<leader>bf", ":NvimTreeFindFile<CR>", { noremap = true, silent = true })
  end,
}
--
-- return {
--   "nvim-neo-tree/neo-tree.nvim",
--   branch = "v3.x",
--   dependencies = {
--     "nvim-lua/plenary.nvim",
--     "nvim-tree/nvim-web-devicons",
--     "MunifTanjim/nui.nvim",
--   },
--   config = function()
--     require('neo-tree').setup({
--       filesystem = {
--         filters = {
--           hide_dotfiles = false, -- Show hidden files
--         },
--       },
--     })
--
--     -- Key mappings
--     vim.keymap.set("n", "<C-n>", ":Neotree filesystem reveal left<CR>", {})
--     vim.keymap.set("n", "<leader>bf", ":Neotree buffers reveal float<CR>", {})
--     vim.keymap.set("n", "<C-e>", ":Neotree toggle<CR>")
--   end,
-- }
