local lazypath = vim.fn.stdpath("data") .. "/lazy/lazy.nvim"
if not vim.loop.fs_stat(lazypath) then
  vim.fn.system({
    "git",
    "clone",
    "--filter=blob:none",
    "https://github.com/folke/lazy.nvim.git",
    "--branch=stable", -- latest stable release
    lazypath,
  })
end
vim.opt.rtp:prepend(lazypath)

vim.api.nvim_exec([[
    augroup transparency
        autocmd!
        autocmd VimEnter * hi Normal guibg=NONE ctermbg=NONE
        autocmd VimEnter * hi NonText guibg=NONE ctermbg=NONE
        autocmd VimEnter * hi LineNr guibg=NONE ctermbg=NONE
    augroup END
]], false)


vim.api.nvim_set_keymap('n', 'yy', '"+yy', { noremap = true, silent = true })
vim.api.nvim_set_keymap('n', 'p', '"+p', { noremap = true, silent = true })
vim.api.nvim_set_keymap('v', 'y', '"+y', { noremap = true, silent = true })
vim.api.nvim_set_keymap('v', 'p', '"+p', { noremap = true, silent = true })
vim.api.nvim_set_keymap('n', 'd', '"+d', { noremap = true, silent = true })
vim.api.nvim_set_keymap('v', 'd', '"+d', { noremap = true, silent = true })
vim.api.nvim_set_keymap('n', 'dd', '"+dd', { noremap = true, silent = true })

vim.opt.guicursor = "n-v-c:block-blinkon100"
vim.opt.fillchars = {eob = " "}
vim.opt.signcolumn = 'no'
require("vim-options")
require("lazy").setup("plugins")
