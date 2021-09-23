# Based on NonTrival-Mips project(https://github.com/trivialmips/nontrivial-mips)
update_compile_order -fileset sources_1

# Check jobs number
if {[info exists env(JOBS_NUMBER)]} {
    set jobs_number $env(JOBS_NUMBER)
} else {
    set jobs_number 2
}

puts "JOBS NUMBER is $jobs_number"

reset_run impl_1
reset_run synth_1
launch_runs -jobs $jobs_number impl_1 -to_step write_bitstream
wait_on_run impl_1

exit